package com.yyds.recipe.controller;

import com.yyds.recipe.utils.MinioUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("upload")
    public String uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();
            String fileName = "test" + "/" + UUIDGenerator.createUserId() + originalFilename.substring(originalFilename.lastIndexOf("."));

            minioUtil.putObject("test", fileName, contentType, inputStream);
            inputStream.close();
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }
}

