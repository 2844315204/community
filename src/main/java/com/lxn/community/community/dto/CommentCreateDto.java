package com.lxn.community.community.dto;

import com.lxn.community.community.bean.User;
import lombok.Data;

@Data
public class CommentCreateDto {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String comment;
    private Integer commentCount;
    private User user;
}
