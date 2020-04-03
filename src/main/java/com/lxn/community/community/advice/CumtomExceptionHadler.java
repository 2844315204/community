package com.lxn.community.community.advice;

import com.alibaba.fastjson.JSON;
import com.lxn.community.community.dto.ResultDto;
import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CumtomExceptionHadler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Throwable e, Model model) {

        String contentType=request.getContentType();
//        判断页面出错  还是局内刷新
        if ("application/json".equals(contentType)){
            ResultDto resultDto;
            //返回json
            if (e instanceof CustomizeException){
                resultDto = ResultDto.errorOf((CustomizeException) e);
            }else {
                resultDto = ResultDto.errorOf(ICustomizeErrorCode.SYS_ERROR);
            }
            try {
               response.setContentType("application/json");
               response.setStatus(200);
               response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            }catch (IOException ioe){
            }
            return null;
        }else {
            //返回html
            if (e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",ICustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }

}
