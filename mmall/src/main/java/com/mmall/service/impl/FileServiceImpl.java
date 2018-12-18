package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        //拿到原始文件名
        String fileName=file.getOriginalFilename();
        //拓展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传的文件名：{}，上传的路径:{},新文件名：{}",fileName,path,uploadFileName);

        File fileDir=new File(path);
        if(!fileDir.exists()){
            //获取读取的权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try{
            file.transferTo(targetFile);
            //文件已成功上传
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //文件已上传到ftp服务器上

            //toto 将targetFile上传到FTP服务器上，
            targetFile.delete();
            //上传成功后，删除upload下面的额文件
        } catch (IOException e){
            logger.error("上传文件失败",e);
            return null;
        }
        return targetFile.getName();
    }

    public String download(MultipartFile file,String path){
        String fileName=file.getOriginalFilename();
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String downFileName=UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始下载文件，下载的文件名：{}，新文件名：{}",fileName,path,downFileName);

        File fileDir=new File(path,downFileName);
        if(!fileDir.exists()){
            //获取读取的权限
            fileDir.setReadable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,downFileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            logger.info("下载文件失败",e);
        }finally {
            return targetFile.getName();
        }
    }

}
