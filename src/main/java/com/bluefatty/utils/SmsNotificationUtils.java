package com.bluefatty.utils;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bluefatty.common.URLType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
@Slf4j
@Component
public class SmsNotificationUtils {
    @Value("${bmob.smsCodeUrl}")
    private String smsCodeUrl;
    @Value("${bmob.applicationId}")
    private String applicationId;
    @Value("${bmob.restfulKey}")
    private String restfulKey;
    @Value("${bmob.verificationTemplate}")
    private String verificationTemplate;

    /**
     * 发送短息
     * @param phoneNumber  电话
     * @param code  验证码
     * @return
     * @throws Exception
     */
    public void sendMessage(String phoneNumber,String code){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Bmob-Application-Id",applicationId);
        headers.put("X-Bmob-REST-API-Key",restfulKey);
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap<String, String>();
        body.put("mobilePhoneNumber",phoneNumber);
        body.put("template",verificationTemplate);
        try{
            String request = HttpUtils.request(smsCodeUrl, null, JSON.toJSONString(body), URLType.HTTPS, "UTF-8", headers);
            log.info("发送短信验证码,phoneNumber={},code={},结果={}",phoneNumber,code,JSON.toJSONString(request));
        }catch (Exception e){
            log.error("发送短信验证码,失败,e={}",phoneNumber,e);
        }

    }
}
