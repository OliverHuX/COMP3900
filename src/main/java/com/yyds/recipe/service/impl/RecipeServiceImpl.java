package com.yyds.recipe.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyds.recipe.exception.AuthorizationException;
import com.yyds.recipe.exception.MySqlErrorException;
import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.CollectionMapper;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.utils.JwtUtil;
import com.yyds.recipe.utils.MinioUtil;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@EnableTransactionManagement
@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Value("${minio.bucket.recipe.photo}")
    private String recipePhotoBucketName;

    @Value("${minio.bucket.recipe.video}}")
    private String recipeVideoBucketName;

    private Helper helper = new Helper();

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public ResponseEntity<?> postRecipe(HttpServletRequest request, MultipartFile[] uploadPhotos, Recipe recipe) {
        User user = checkedUser(request);
        String recipeId = UUIDGenerator.createRecipeId();
        recipe.setRecipeId(recipeId);
        recipe.setCreateTime(String.valueOf(System.currentTimeMillis()));
        recipe.setUserId(user.getUserId());

        // insert into recipe table
        try {
            recipeMapper.saveRecipe(recipe);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        recipe.setRecipePhotos(new ArrayList<>());
        for (MultipartFile uploadPhoto : uploadPhotos) {
            String originalFilename = uploadPhoto.getOriginalFilename();
            if (originalFilename == null) {
                continue;
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String contentType = uploadPhoto.getContentType();
            InputStream inputStream = null;
            try {
                inputStream = uploadPhoto.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String photoName = UUIDGenerator.generateUUID() + suffix;
            minioUtil.putObject(recipePhotoBucketName, photoName, contentType, inputStream);
            recipe.getRecipePhotos().add(photoName);
        }

        // insert into photo table
        try {
            recipeMapper.savePhotos(recipeId, recipe.getRecipePhotos());
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> likeRecipe(HttpServletRequest request, Recipe recipe) {

        User user = checkedUser(request);

        String recipeId = recipe.getRecipeId();

        Recipe checkRecipe = recipeMapper.getRecipeById(recipeId);
        if (checkRecipe == null) {
            return ResponseUtil.getResponse(ResponseCode.RECIPE_ID_NOT_FOUND, null, null);
        }

        try {
            recipeMapper.likeRecipe(user.getUserId(), recipeId);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> unlikeRecipe(HttpServletRequest request, Recipe recipe) {

        User user = checkedUser(request);

        String recipeId = recipe.getRecipeId();

        Recipe checkRecipe = recipeMapper.getRecipeById(recipeId);
        if (checkRecipe == null) {
            return ResponseUtil.getResponse(ResponseCode.RECIPE_ID_NOT_FOUND, null, null);
        }

        try {
            recipeMapper.unlikeRecipe(user.getUserId(), recipeId);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> getAllPublicRecipes(int pageNum, int pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize >= 9) {
            pageSize = 9;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<Recipe> recipeList = recipeMapper.getRecipeList();
        PageInfo<Recipe> recipePageInfo = new PageInfo<>(recipeList);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipe lists", recipeList);
        resultMap.put("total", recipePageInfo.getTotal());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> getMyRecipes(int pageNum, int pageSize, HttpServletRequest request) {
        User user = checkedUser(request);
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize >= 9) {
            pageSize = 9;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<Recipe> myRecipeList = recipeMapper.getMyRecipeList(user.getUserId());
        for (Recipe recipe : myRecipeList) {
            recipe.setRecipePhotos(new ArrayList<>());
            List<String> recipePhotos = recipe.getRecipePhotos();
            String recipeId = recipe.getRecipeId();
            List<String> fileNameList = recipeMapper.getFileNameListByRecipeId(recipeId);
            recipePhotos.addAll(fileNameList);

            List<String> base64Strings = new ArrayList<>();
            for (String file : fileNameList) {
                InputStream inputStream = minioUtil.getObject(recipePhotoBucketName, file);
                byte[] bytes = new byte[0];
                try {
                    bytes = IOUtils.toByteArray(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = Base64.getEncoder().encodeToString(bytes);
                base64Strings.add(s);
            }
            recipe.setBase64photoList(base64Strings);
        }
        PageInfo<Recipe> recipePageInfo = new PageInfo<>(myRecipeList);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipe lists", myRecipeList);
        resultMap.put("total", recipePageInfo.getTotal());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> commentRecipe(String viewerUserId, String recipeId, String comment) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError != null) {
            return recipeError;
        }

        if (comment == null) {
            return ResponseUtil.getResponse(ResponseCode.PARAMETER_ERROR, null, null);
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.addComment(viewerUserId, comment);

        try {
            recipeMapper.updateRecipeComments(recipeId, recipe.getComments());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> deleteComment(String viewerUserId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError != null) {
            return recipeError;
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.deleteComment(viewerUserId);

        try {
            recipeMapper.updateRecipeComments(recipeId, recipe.getComments());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> summaryRecipe(Recipe recipe) {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (Objects.nonNull(recipeError)) {
            resultMap.put("recipeError", recipeError);
            // return ResponseUtil.getResponse(ResponseCode.ERROR, null, resultMap);
            return null;
        }

        resultMap.put("summary", recipe.getIntroduction());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> subscribeRecipe(User viewer, Recipe recipe) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            // return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
            return null;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (recipeError != null) {
            return recipeError;
        }

        try {
            viewer.getSubscribes().add(recipe.getRecipeId());
            recipeMapper.updateSubscribe(viewer.getUserId(), viewer.getSubscribes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> cancelSubscribeRecipe(User viewer, Recipe recipe) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            // return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
            return null;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (recipeError != null) {
            return recipeError;
        }

        try {
            viewer.getSubscribes().remove(recipe.getRecipeId());
            recipeMapper.deleteSubscribe(viewer.getUserId(), viewer.getSubscribes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> setPrivacyRecipe(HttpServletRequest request, Recipe recipe) {
        User user = checkedUser(request);
        String recipeId = recipe.getRecipeId();

        Recipe checkedRecipe = recipeMapper.getRecipeById(recipeId);
        if (checkedRecipe == null) {
            return ResponseUtil.getResponse(ResponseCode.RECIPE_ID_NOT_FOUND, null, null);
        }

        if (!StringUtils.equals(user.getUserId(), checkedRecipe.getUserId())) {
            throw new AuthorizationException();
        }

        try {
            recipeMapper.updatePrivacy(recipeId, recipe.getIsPrivacy());
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> collectRecipe(String viewerUserId, String collectionId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(collectionId);
        if (collectionError != null) {
            return collectionError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError != null) {
            return recipeError;
        }

        User viewer = userMapper.getUserByUserId(viewerUserId);
        Collection collection = collectionMapper.getCollectionById(collectionId);
        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        collection.addRecipe(recipe);

        HashMap<String, Collection> collections = viewer.getCollections();
        collections.put(collectionId, collection);

        try {
            userMapper.updateCollections(viewerUserId, collections);
            collectionMapper.updateCollectionRecipes(collectionId, collection.getRecipes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);

    }

    private User checkedUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new AuthorizationException();
        }
        String userId = JwtUtil.decodeToken(token).getClaim("userId").asString();
        User checkedUser = userMapper.getUserByUserId(userId);
        if (checkedUser == null) {
            throw new AuthorizationException();
        }
        return checkedUser;
    }
}
