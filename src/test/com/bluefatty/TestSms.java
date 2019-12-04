package com.bluefatty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bluefatty.common.URLType;
import com.bluefatty.utils.HttpUtils;
import com.bluefatty.utils.PhoneCodeUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-12-02
 */
public class TestSms {
    @Test
    public void fun1(){
        String code = PhoneCodeUtils.getVerifyCode();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Bmob-Application-Id","082a9f95605864c0ecc81f73685a72e9");
        headers.put("X-Bmob-REST-API-Key","b714bd42772721b9ba1ce92fea15831c");
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap<String, String>();
        body.put("mobilePhoneNumber","15859706798");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("%smscode%",code);
        jsonObject.put("%ttl%","1");
        jsonObject.put("%appname%","彩色笔记");
        body.put("template","彩色笔记");
        try{
            String request = HttpUtils.request("https://api2.bmob.cn/1/requestSmsCode", null, JSON.toJSONString(body), URLType.HTTPS, "UTF-8", headers);
            System.out.println("发送短信验证码 结果="+JSON.toJSONString(request));
        }catch (Exception e){
           e.printStackTrace();
            //System.out.println("发送短信验证码,失败,e={}",phoneNumber,e);
        }
    }
}
