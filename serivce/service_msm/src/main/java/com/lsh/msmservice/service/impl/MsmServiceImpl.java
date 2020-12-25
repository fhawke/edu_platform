package com.lsh.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lsh.msmservice.service.MsmService;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    /**
     * 发送短信
     */
    public boolean send(String PhoneNumbers, String templateCode, Map<String,Object> param) {

        if(StringUtils.isEmpty(PhoneNumbers)) return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4GFbjHVJWCnTiBJdhUZm", "g56Hzz5pU8UVTmQXbqlgI5vUUtewIt");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", PhoneNumbers);
        request.putQueryParameter("SignName", "Halo博客");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}