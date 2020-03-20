package com.lxn.community.community.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class GithubUser implements Serializable {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}
