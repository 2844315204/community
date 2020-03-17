package com.lxn.community.community.controller;

import com.lxn.community.community.bean.AccessToken;
import com.lxn.community.community.bean.GithubUser;
import com.lxn.community.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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
        GithubUser user = gitHubProvider.githubUser(token);
        System.out.println(user.getName());
        if (user!=null){
//            登陆成功
            request.setAttribute("user",user);
            return "redirect:index";
        }else {
//            登录失败
            return "redirect:index";
        }
    }
}
