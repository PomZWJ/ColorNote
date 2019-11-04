package com.bluefatty.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public class TbUser implements Serializable {
    /**
     * 用户Id(手机号码)
     */
    @Setter
    @Getter
    private String userId;
    /**
     * 用户名称
     */
    @Setter
    @Getter
    private String userName;
    /**
     * 创建日期yyyyMMdd
     */
    @Setter
    @Getter
    private String createDate;
    /**
     * 创建时间HHmmss
     */
    @Setter
    @Getter
    private String createTime;
    /**
     * 头像URL
     */
    @Setter
    @Getter
    private String iconUrl;
    /**
     * 账户状态
     * 0--正常
     * 1--注销
     * 2--冻结
     */
    @Setter
    @Getter
    private String accountStatus;
}