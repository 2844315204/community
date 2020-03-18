package com.lxn.community.community.controller;

import com.lxn.community.community.bean.AccessToken;
import com.lxn.community.community.bean.GithubUser;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.provider.GitHubProvider;
import com.lxn.community.community.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String clientUri;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
//        1.获取access_token值
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(clientId);
        accessToken.setClient_secret(clientSecret);
        accessToken.setCode(code);
        accessToken.setRedirect_uri(clientUri);
        accessToken.setState(state);
        String token = gitHubProvider.getAccessToken(accessToken);
//        获取用户名字
        GithubUser githubUser = gitHubProvider.githubUser(token);
        if (githubUser!=null){

            User user = new User();
//            获取随机token
            user.setToken(UUID.randomUUID().toString());
//            获取github的名字
            user.setName(githubUser.getName());
//             github的用户名(github的id)
            user.setAccountId(String.valueOf(githubUser.getId()));
//              当前时间的毫秒数
            user.setGmtCreate(System.currentTimeMillis());
//            结束时间的毫秒数
            user.setGmtModified(user.getGmtCreate());
            userService.insertUser(user);
//            登陆成功 写session和cookie
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";
        }else {
//            登录失败
            return "redirect:/";
        }
    }
}
