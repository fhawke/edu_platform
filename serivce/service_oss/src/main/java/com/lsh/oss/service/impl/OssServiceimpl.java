package com.lsh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lsh.oss.service.OssService;
import com.lsh.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceimpl implements OssService {

    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
    try{
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 获取上传文件流。
        InputStream inputStream = file.getInputStream();

        //获取文件名称
        String filename = file.getOriginalFilename();

        //文件名添加随机值
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        filename = uuid + filename;

        //把文件按照日期分类
        //获取当前日期
        String datePath = new DateTime().toString("yyyy/MM/dd");


        //拼接 2020/11/12/ew.jpg
        filename = datePath + "/" + filename;
        //调用oss方法实现上传
        ossClient.putObject(bucketName, filename, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();


        String url = "https://"+bucketName+"."+endpoint+"/"+filename;
        return url;
        }catch (Exception e){

        }
// 创建OSSClient实例。

        return null;
    }
}
