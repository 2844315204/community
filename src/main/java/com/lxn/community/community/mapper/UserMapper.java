package com.lxn.community.community.mapper;

import com.lxn.community.community.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insertUser(User user);
}
