package com.lxn.community.community.controller;

import com.lxn.community.community.dto.PageDto;
import com.lxn.community.community.service.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {
    @Autowired
    private QuestionServiceImpl questionService;
    @GetMapping("/")
    public String index( Model model,
                        @RequestParam( value = "page", defaultValue = "1" ) Integer page,
                        @RequestParam( value = "search",required = false) String search,
                        @RequestParam( value = "size", defaultValue = "7" ) Integer size){
        PageDto pageInfo= questionService.listQuestion(search,page,size);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("search",search);

        return "index";
    }
}
