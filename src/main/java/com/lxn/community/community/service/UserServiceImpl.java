package com.lxn.community.community.service;

import com.lxn.community.community.bean.User;
import com.lxn.community.community.bean.UserExample;
import com.lxn.community.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl{

    @Autowired(required = false)
    UserMapper mapper;


    public void createUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().
                andAccountIdEqualTo(user.getAccountId());
        List<User> users = mapper.selectByExample(userExample);
        if (users.size()==0){
//              当前时间的毫秒数
          user.setGmtCreate(System.currentTimeMillis());
//            结束时间的毫秒数
 //          插入数据
          user.setGmtModified(user.getGmtCreate());
            mapper.insert(user);
      }else {
            User dbuser = users.get(0);
            User updateuser = new User();
            updateuser.setGmtModified(System.currentTimeMillis());
            updateuser.setAvatarUrl(user.getAvatarUrl());
            updateuser.setName(user.getName());
            updateuser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().
                    andIdEqualTo(dbuser.getId());
            mapper.updateByExampleSelective(updateuser, example);
//          更新数据

      }
    }
}
