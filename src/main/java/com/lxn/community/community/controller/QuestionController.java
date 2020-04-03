package com.lxn.community.community.controller;

import com.lxn.community.community.dto.CommentCreateDto;
import com.lxn.community.community.dto.QuestionDto;
import com.lxn.community.community.enums.CommentTypeEnum;
import com.lxn.community.community.service.CommentService;
import com.lxn.community.community.service.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id")Long id, Model model){
       QuestionDto questionDto= questionService.questionId(id);
       List<QuestionDto> relatedQuestion =questionService.selectRelated(questionDto);
       List<CommentCreateDto> comments= commentService.listByTargetId(id,CommentTypeEnum.QUESTION);
//       累加评论
       questionService.incView(id);
        model.addAttribute("question",questionDto);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestion",relatedQuestion);
        return "question";
    }

}
