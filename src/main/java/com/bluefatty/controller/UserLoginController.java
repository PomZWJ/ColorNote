package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bluefatty.domain.Token;
import com.bluefatty.domain.CnUser;
import com.bluefatty.service.UserService;
import com.bluefatty.utils.DateUtils;
import com.bluefatty.utils.Md5Utils;
import com.bluefatty.utils.RedisUtils;
import com.bluefatty.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 用户登录Controller
 *
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
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String userId, String password) {
        CnUser user = userService.selectByPrimaryKey(userId);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                user.setPassword(null);
                Token token = new Token();
                token.setCreateDate(DateUtils.getCurrentDate());
                token.setCreateTime(DateUtils.getCurrentTime());
                token.setUser(user);
                token.setSign(Md5Utils.getMd5Str(JSON.toJSONString(user)));
                String tokenStr = RsaUtils.rsaPublicEncrypt(JSON.toJSONString(token));
                redisUtils.cacheString("token_user_" + userId, tokenStr, 3600, TimeUnit.SECONDS);
                return "登录成功";
            } else {
                return "密码错误";
            }
        } else {
            return "用户不存在";
        }
    }

    @RequestMapping(value = "/getCurrentLoginUserInfo", method = RequestMethod.POST)
    public Object getCurrentLoginUserInfo(String userId, String token) {
        String cacheString = redisUtils.getCacheString("token_user_" + userId);
        if (cacheString.equals(token)) {
            String tokenStr1 = RsaUtils.rsaPrivateKeyDecrypt(token);
            String tokenStr2 = RsaUtils.rsaPrivateKeyDecrypt(cacheString);
            Token token1 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr1), Token.class);
            Token token2 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr2), Token.class);

            if (!token1.getSign().equals(token2.getSign())) {
                return "验签失败";
            } else {
                return tokenStr2;
            }
        } else {
            return "token不存在";
        }
    }
    @RequestMapping(value = "/wxPubLogin",method = RequestMethod.POST)
    public void wxPubLogin(){
        return;
    }
}
