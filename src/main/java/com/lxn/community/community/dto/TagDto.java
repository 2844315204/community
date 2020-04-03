package com.lxn.community.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private String categoryName;//外层标签
    private List<String> tags;
}
