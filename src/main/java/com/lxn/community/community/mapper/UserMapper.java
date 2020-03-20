package com.lxn.community.community.mapper;

import com.lxn.community.community.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
//    查询用户
    int insertUser(User user);

//    查询token
    User selectToken(@Param("token")String token);

    User findById(@Param("id")Integer id);
}
