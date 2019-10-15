package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.domain.User;
import com.bluefatty.utils.RedisUtils;
import com.bluefatty.utils.RsaUtils;
import com.bluefatty.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录Controller
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserLoginController {
    @Autowired
    private RedisUtils redisUtils;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String userId,String password){
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        if(equals(userId)&&"111".equals(password)){
            String token_user_id_1 = redisUtils.getCacheString("token_user_id_1");
            System.out.println(token_user_id_1);
            System.out.println("111");
            if(!StringUtils.isEmpty(token_user_id_1)){
                return "登录成功";
            }
            Map<String,String> map = new HashMap<>(10);
            map.put("id","1");
            map.put("userName","zwj");
            map.put("password","111");
            String token = RsaUtils.rsaPublicEncrypt(JSON.toJSONString(map));
            redisUtils.cacheString("token_user_id_1",token,3600, TimeUnit.SECONDS);
            return "登录成功";
        }else{
            return "登录失败";
        }
    }
}
