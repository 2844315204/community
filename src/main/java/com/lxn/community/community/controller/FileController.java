package com.lxn.community.community.controller;

import com.lxn.community.community.dto.FileDto;
import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import com.lxn.community.community.provider.AliyunProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private AliyunProvider aliyunProvider;

    @RequestMapping("/file/unload")
    @ResponseBody
    public FileDto upload(HttpServletRequest request){

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        FileDto fileDto = new FileDto();
        try {
            String url = aliyunProvider.fileUpload(file.getInputStream(), file.getOriginalFilename());
            fileDto.setUrl(url);
            fileDto.setSuccess(1);
            fileDto.setMessage("上传成功");
        } catch (IOException e) {
            fileDto.setSuccess(0);
            fileDto.setMessage("上传失败");
            e.printStackTrace();
            throw new CustomizeException(ICustomizeErrorCode.UPLOAD_FILE_ERROR);
        }
        return fileDto;
    }
}
