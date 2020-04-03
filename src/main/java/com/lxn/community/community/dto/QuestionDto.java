package com.lxn.community.community.dto;

import com.lxn.community.community.bean.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
