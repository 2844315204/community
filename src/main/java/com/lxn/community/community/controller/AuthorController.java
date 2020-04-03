package com.lxn.community.community.controller;

import com.lxn.community.community.dto.AccessToken;
import com.lxn.community.community.dto.GithubUser;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.provider.GitHubProvider;
import com.lxn.community.community.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
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
                           HttpServletResponse response){
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
        if (githubUser!=null && githubUser.getId()!=null){
            User user = new User();
//            获取随机token
            String token1 = UUID.randomUUID().toString();
            user.setToken(token1);
//            获取github的名字
            user.setName(githubUser.getName());
//             github的用户名(github的id)
            user.setAccountId(String.valueOf(githubUser.getId()));
//          获取头像
            user.setAvatarUrl(githubUser.getAvatar_url());
//            判断当前用户 数据库中是否存在 没有就创建  有就修改
            userService.createUpdate(user);
//          添加一个自定义cookie
            response.addCookie(new Cookie("token",token1));
            return "redirect:/";
        }else {
//            返回错误日志
            log.error("callback get github error,{}"+githubUser);
//            登录失败
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");//移除session
        Cookie cookie = new Cookie("token",null);//设置cookie值为null
        cookie.setMaxAge(0);//设置过期时间
        response.addCookie(cookie);//替换cookie值（作用：移除cookie）
        return "redirect:/";
    }
}
