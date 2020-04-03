package com.lxn.community.community.mapper;

import com.lxn.community.community.bean.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentExMapper {

    int inextCommentCount(Comment record);
}