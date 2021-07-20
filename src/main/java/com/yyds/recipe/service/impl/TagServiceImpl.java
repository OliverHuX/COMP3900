package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.MySqlErrorException;
import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.TagMapper;
import com.yyds.recipe.model.Tag;
import com.yyds.recipe.service.TagService;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseEntity<?> getTags() {
        List<Tag> tagsList = null;
        try {
            tagsList = tagMapper.getTagsList();
        } catch (Exception e) {
            throw new MySqlErrorException();
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("tags", tagsList);
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, res);
    }
}
