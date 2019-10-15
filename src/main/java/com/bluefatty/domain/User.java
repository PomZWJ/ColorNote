package com.bluefatty.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户头像
     */
    private String iconUrl;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建日期
     */
    private String createDate;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * E-mail
     */
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 账户状态
     */
    private String accountStatus;
}
