package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> getTagsList();
}
