package com.bluefatty.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Data
public class CnUser implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private String createDate;
    private String createTime;
    private String email;
    private String phone;
    private String iconUrl;
    private String accountStatus;
}