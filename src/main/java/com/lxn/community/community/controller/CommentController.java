package com.lxn.community.community.controller;

import com.lxn.community.community.bean.Comment;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.dto.CommentCreateDto;
import com.lxn.community.community.dto.CommentDto;
import com.lxn.community.community.dto.QuestionDto;
import com.lxn.community.community.dto.ResultDto;
import com.lxn.community.community.enums.CommentTypeEnum;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import com.lxn.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired(required = false)
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDto commentDto,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDto.errorOf(ICustomizeErrorCode.NO_LOGININ);
        }
        if (commentDto==null|| StringUtils.isBlank(commentDto.getContent())){
            return ResultDto.errorOf(ICustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDto.getParentId());
        comment.setComment(commentDto.getContent());
        comment.setType(commentDto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment,user);
        return ResultDto.okoff();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List<CommentCreateDto>> comments(@PathVariable("id")Long id) {
        List<CommentCreateDto> comments = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDto.okoff(comments);
    }
}
