package com.lxn.community.community.service;

import com.lxn.community.community.bean.User;

public interface UserService {
    int insertUser(User user);

    User selectToken(String token);

    User findById(Integer id);
}
