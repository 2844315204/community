package com.lxn.community.community.service;

import com.lxn.community.community.bean.User;
import com.lxn.community.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired(required = false)
    UserMapper mapper;
    @Override
    public int insertUser(User user) {
        return mapper.insertUser(user);
    }

    @Override
    public User selectToken(String token) {
        return mapper.selectToken(token);
    }

    @Override
    public User findById(Integer id) {
        return mapper.findById(id);
    }
}
