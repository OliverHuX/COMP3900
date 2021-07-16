package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private Integer gender;
    private String email;
    private String nickName;
    private String password;
    private String birthdate;
    private String createTime;
    private String profilePhoto;
    private List<String> subscribes;
    private HashMap<String, Collection> collections;
    private String salt;

    public void addCollection(Collection collection) {
        collections.put(collection.getCollectionId(), collection);
    }

    public void removeCollection(String collectionId) {
        collections.remove(collectionId);
    }

    public Collection findCollectionById(String collectionId) {
        return collections.get(collectionId);
    }

    public boolean isSubscribedTo(String creator) {
        return subscribes.contains(creator);
    }
}
