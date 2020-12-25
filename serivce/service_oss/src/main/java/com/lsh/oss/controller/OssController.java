package com.lsh.oss.controller;


import com.lsh.commonutils.util.R;
import com.lsh.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("eduoss/fileoss")

public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像
    @ApiOperation(value = "上传文件")
    @PostMapping
    public R uploadOssFile(MultipartFile file){

        //获取上传文件  MultipartFile
        String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url",url);
    }

}
