package com.leaf.file.controller;

import com.leaf.common.result.Result;
import com.leaf.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
public class MinioController {

    @Autowired
    IFileService fileService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file, String basDir) {

        try {
            String url = fileService.upload(file, basDir);
            Result success = Result.success();
            success.setData(url);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }

    }
}
