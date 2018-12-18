package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService  {

    String upload(MultipartFile file, String path);

    String download(MultipartFile file,String path);

    }
