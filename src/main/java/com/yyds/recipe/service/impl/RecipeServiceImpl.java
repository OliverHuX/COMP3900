package com.yyds.recipe.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyds.recipe.exception.AuthorizationException;
import com.yyds.recipe.exception.MySqlErrorException;
import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.CollectionMapper;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.Comment;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.utils.JwtUtil;
import com.yyds.recipe.utils.MinioUtil;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
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
import java.text.SimpleDateFormat;
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


    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public ResponseEntity<?> postRecipe(HttpServletRequest request, MultipartFile[] uploadPhotos, Recipe recipe,
                                        MultipartFile[] uploadVideos) {
        User user = checkedUser(request);
        String recipeId = UUIDGenerator.createRecipeId();
        recipe.setRecipeId(recipeId);
        recipe.setCreateTime(String.valueOf(System.currentTimeMillis()));
        recipe.setUserId(user.getUserId());

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

        // insert into recipe table
        List<String> tagList = recipe.getTags();
        try {
            recipeMapper.saveRecipe(recipe);
            recipeMapper.saveTagRecipe(recipeId, tagList);
        } catch (Exception e) {
            throw new MySqlErrorException();
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
    public ResponseEntity<?> getAllPublicRecipes(String recipeId,
                                                 String creatorId,
                                                 String searchContent,
                                                 String searchTags,
                                                 Integer pageNum,
                                                 Integer pageSize,
                                                 HttpServletRequest request) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize >= 9) {
            pageSize = 9;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        if (searchTags != null) {

        }
        List<String> searchTagList = null;
        if (searchTags != null) {
            searchTagList = Arrays.asList(searchTags.split(","));
        }
        String userId = JwtUtil.decodeToken(request.getHeader("token")).getClaim("userId").asString();
        List<Recipe> recipeList = recipeMapper.getRecipeList(searchTagList, searchContent, creatorId, recipeId, userId);
        for (Recipe recipe : recipeList) {
            List<String> recipePhotos = new ArrayList<>();
            List<String> fileNameList = recipeMapper.getFileNameListByRecipeId(recipe.getRecipeId());
            for (String fileName : fileNameList) {
                String fileUrl = minioUtil.presignedGetObject(recipePhotoBucketName, fileName, 7);
                recipePhotos.add(fileUrl);
            }
            recipe.setRecipePhotos(recipePhotos);
            List<String> tags = recipeMapper.getTagListByRecipeId(recipe.getRecipeId());
            recipe.setTags(tags);
        }
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
            List<String> recipePhotos = new ArrayList<>();
            List<String> fileNameList = recipeMapper.getFileNameListByRecipeId(recipe.getRecipeId());
            for (String fileName : fileNameList) {
                String fileUrl = minioUtil.presignedGetObject(recipePhotoBucketName, fileName, 7);
                recipePhotos.add(fileUrl);
            }
            recipe.setRecipePhotos(recipePhotos);
            List<String> tags = recipeMapper.getTagListByRecipeId(recipe.getRecipeId());
            recipe.setTags(tags);
        }
        PageInfo<Recipe> recipePageInfo = new PageInfo<>(myRecipeList);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipe lists", myRecipeList);
        resultMap.put("total", recipePageInfo.getTotal());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> commentRecipe(Comment comment, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new AuthorizationException();
        }
        comment.setCommentId(UUIDGenerator.generateUUID());
        comment.setCreatorId(JwtUtil.decodeToken(token).getClaim("userId").asString());
        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        comment.setCreateTime(currentTimeString);
        comment.setUpdateTime(currentTimeString);
        try {
            recipeMapper.postComment(comment);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> deleteComment(Comment comment, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new AuthorizationException();
        }
        String commentId = comment.getCommentId();
        if (commentId == null) {
            throw new AuthorizationException();
        }
        String userId = JwtUtil.decodeToken(token).getClaim("userId").asString();
        List<Comment> CommentList = recipeMapper.getComments(commentId);

        try {
            recipeMapper.deleteComment(comment);
        } catch (Exception e) {
            throw new MySqlErrorException();
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


    @Override
    public ResponseEntity<?> rateRecipe(Recipe recipe, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new AuthorizationException();
        }
        String userId = JwtUtil.decodeToken(token).getClaim("userId").asString();
        String recipeId = recipe.getRecipeId();
        if (recipeMapper.getRecipeById(recipeId) == null) {
            return ResponseUtil.getResponse(ResponseCode.RECIPE_ID_NOT_FOUND, null, null);
        }
        try {
            int count = recipeMapper.getCountBySpecificRate(recipeId, userId);
            if (count > 0) {
                recipeMapper.updateRate(recipeId, userId, recipe.getRateScore());
            } else {
                recipeMapper.rateRecipe(recipeId, userId, recipe.getRateScore());
            }
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }
}
