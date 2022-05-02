package com.leaf.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {

    String upload(MultipartFile file, String basDir) throws Exception;
}
