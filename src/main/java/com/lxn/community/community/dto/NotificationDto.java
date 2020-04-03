package com.lxn.community.community.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;//通知的人id
    private String notifierName;//通知的人
    private String outerTitle;
    private Long outerId;
    private String typeName;
    private Integer type;
}
