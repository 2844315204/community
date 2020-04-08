package com.lxn.community.community.service;

import com.lxn.community.community.bean.*;
import com.lxn.community.community.dto.CommentCreateDto;
import com.lxn.community.community.enums.CommentTypeEnum;
import com.lxn.community.community.enums.NotificationStatusEnum;
import com.lxn.community.community.enums.NotificationTypeEnum;
import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import com.lxn.community.community.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private QuestionExMapper questionExMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private CommentExMapper commentExMapper;
    @Autowired(required = false)
    private NotificationMapper notificationMapper;

//    事务回滚（出错）
    @Transactional
    public void insert(Comment comment,User commentator) {
//        判断是否回复
        if (comment.getParentId()==null ||comment.getParentId()==0){
            throw new CustomizeException(ICustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
//        判断返回一二级回复类型
        if (comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(ICustomizeErrorCode.TYPE_PARAM_WRONG);
        }
//         判断返回问题还是返回评论
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbcomment==null){
                throw new CustomizeException(ICustomizeErrorCode.COMMENT_NOT_FOUND);
            }

//            回复问题
            Question question = questionMapper.selectByPrimaryKey(dbcomment.getParentId());
            if (question==null){
                throw new CustomizeException(ICustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
//          增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount
                    (1);
            commentExMapper.inextCommentCount(parentComment);

//            创建通知
            createNotify(comment, dbcomment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomizeException(ICustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExMapper.extCommentCount(question);
            //            创建通知
            createNotify(comment, question.getCreator(),commentator.getName() ,question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getId());

        }

    }

    private void createNotify(Comment comment, Long commentator, String notifierName, String outorTitle, NotificationTypeEnum notificationTypeEnum, Long outerId) {
        if (commentator ==comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
//            接收到问题的ID
        notification.setOuterId(outerId);
//            发送人的ID
        notification.setNotifler(comment.getCommentator());
//            收到人的ID
        notification.setReceiver(commentator);
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(notificationTypeEnum.getType());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outorTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentCreateDto> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
//        按时间倒序排列
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);

        if (comments.size()==0){
            return new ArrayList<>();
        }
//        获取去重评论人
//        把获取的 ‘评论人ID’ 封装到map集合中，返回一个set集合 set无重复有排序，list无排序有重复有顺序
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

//        把评论人封装到map中
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);

        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

//        转换comment为commentCreateDto
        List<CommentCreateDto> commentCreateDtos = comments.stream().map(comment -> {
            CommentCreateDto commentCreateDto = new CommentCreateDto();
            BeanUtils.copyProperties(comment,commentCreateDto);
            commentCreateDto.setUser(userMap.get(comment.getCommentator()));
            return commentCreateDto;
        }).collect(Collectors.toList());

        return commentCreateDtos;
    }
}
