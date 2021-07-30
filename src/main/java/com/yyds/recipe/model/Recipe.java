package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Recipe implements Serializable {
    private String recipeId;
    private String introduction;
    private String title;
    private String ingredients;
    private String method;
    private List<String> tags;
    private int timeDuration;
    private List<String> recipePhotos;
    private List<String> recipeVideos;
    // TODO: delete it !!!
    private HashMap<String, String> comments;
    private int likes;
    private String userId;
    private String createTime;
    private int isPrivacy;
    private Double rate;
    private Integer isLike;
    private Integer isRate;

    public void addComment(String userId, String comment) {
        comments.put(userId, comment);
    }

    public void deleteComment(String userId) {
        comments.remove(userId);
    }
}
