package com.lxn.community.community.controller;

import com.lxn.community.community.bean.Notification;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.dto.NotificationDto;
import com.lxn.community.community.dto.PageDto;
import com.lxn.community.community.service.NotificationService;
import com.lxn.community.community.service.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action")String action, Model model,
                           @RequestParam( value = "page", defaultValue = "1" ) Integer page,
                          @RequestParam( value = "size", defaultValue = "5" ) Integer size ){

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }

        if ("question".equals(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的问题");
            PageDto pageInfo = questionService.selectId(user.getId(), page, size);
            model.addAttribute("pageInfo", pageInfo);
        }else if ("replies".equals(action)){
            PageDto pageInfo = notificationService.list(user.getId(), page, size);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        return "profile";
    }

}


