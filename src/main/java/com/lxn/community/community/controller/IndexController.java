package com.lxn.community.community.controller;

import com.lxn.community.community.bean.User;
import com.lxn.community.community.dto.QuestionDto;
import com.lxn.community.community.service.QuestionServiceImpl;
import com.lxn.community.community.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    private QuestionServiceImpl questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
//        获取页面cookies，实现持久化登录
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userService.selectToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        List<QuestionDto> questionDtoList= questionService.listQuestion();
        model.addAttribute("questions",questionDtoList);
        return "index";
    }
}
