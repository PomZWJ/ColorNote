package com.bluefatty.service;

import com.bluefatty.domain.TbUser;
import com.bluefatty.domain.Token;

import java.util.Map;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public interface IUserService {
    /**
     * 发送e-mail验证码
     * @param userId
     */
    void sendEmailVerificationCode(String userId);

    /**
     * 用户登录--如果用户不存在，则自动创建
     * @param userId
     * @param verificationCode
     * @return
     */
    Map<String,String> userLogin(String userId, String verificationCode);

    /**
     * 获取用户信息
     * @param userId
     * @param token
     * @return
     */
    Token getCurrentLoginUserInfo(String userId, String token);
}
