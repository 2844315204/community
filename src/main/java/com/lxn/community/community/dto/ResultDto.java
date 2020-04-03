package com.lxn.community.community.dto;

import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import lombok.Data;

@Data
public class ResultDto<T> {
    private Integer code;//状态码
    private String message;//传到前端消息
    private T data;//可以传递各种类型的 泛型

    public static  ResultDto  errorOf(Integer code,String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(ICustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(),errorCode.getMessage());

    }
    public static ResultDto errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }

    public static ResultDto okoff() {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
    public static <T> ResultDto okoff(T t) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }


}
