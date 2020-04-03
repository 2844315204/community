package com.lxn.community.community.exception;

public enum ICustomizeErrorCode implements CustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不存在，换一个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选择任何问题评论进行回复"),
    NO_LOGININ(2003,"未登录，请先登录！"),
    SYS_ERROR(2004,"服务器出错！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"回复的问题不存在，换一个试试？"),
    COMMENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFCATION_FAIL(2008,"兄弟你是读别人信息？"),
    NOTIFCATION_NOT_FOUNT(2009,"消息不翼而飞啦？"),
    UPLOAD_FILE_ERROR(2010,"上传文件失败"),
    ;


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;


    ICustomizeErrorCode(Integer code,String message) {
        this.code = code;
        this.message = message;
    }
}
