package com.lxn.community.community.service;

import com.lxn.community.community.bean.*;
import com.lxn.community.community.dto.NotificationDto;
import com.lxn.community.community.dto.PageDto;
import com.lxn.community.community.enums.NotificationStatusEnum;
import com.lxn.community.community.enums.NotificationTypeEnum;
import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import com.lxn.community.community.mapper.NotificationMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    public PageDto list(Long userId, Integer page, Integer size) {
        PageDto<NotificationDto> pageDto = new PageDto<>();
        Integer totalPage;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalcount = (int)notificationMapper.countByExample(notificationExample);//数据总数
        //        判断多少页
        totalPage=totalcount % size==0 ? totalcount / size : totalcount / size+1;

//      判断页面数是否越界
        if (page<1){
            page=1;
        }
        if (totalPage>0){
            if (page>totalPage){
                page=totalPage;
            }
        }
        pageDto.setPagination(totalPage,page);

        Integer offset=size*(page-1);//行偏移的数量  limit 2,3  检索 3-5行  mysql初始下标0
         NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications =
                notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        if (notifications.size()==0){
            return pageDto;
        }

        List<NotificationDto> notificationDtoList=new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification,notificationDto);
            notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDtoList.add(notificationDto);
        }

        pageDto.setData(notificationDtoList);
        return pageDto;
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId).
                andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDto read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification==null){
            throw new CustomizeException(ICustomizeErrorCode.NOTIFCATION_NOT_FOUNT);
        }
        if (!Objects.equals( notification.getReceiver(),user.getId())){
            throw new CustomizeException(ICustomizeErrorCode.READ_NOTIFCATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification,notificationDto);
        notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDto;
    }
}
