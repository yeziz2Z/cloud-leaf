package com.leaf.file.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.leaf.file.config.prop.MinioProp;
import com.leaf.file.service.IFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioFileServiceImpl implements IFileService {

    @Autowired
    MinioProp minioProp;
    @Autowired
    MinioClient minioClient;

    @Override
    public String upload(MultipartFile file) throws Exception {
        String filename = DateUtil.today() + "/" + file.getOriginalFilename();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioProp.getBucket())
                .object(filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        minioClient.putObject(args);
        return minioProp.getEndpoint() + "/" + minioProp.getBucket() + "/" + filename;
    }
}
