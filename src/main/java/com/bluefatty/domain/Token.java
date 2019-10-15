package com.bluefatty.domain;

import lombok.Data;

/**
 * Token
 *
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Data
public class Token {
    /**
     * 签名
     */
    private String sign;
    /**
     * 用户实体
     */
    private User user;
    /**
     * 创建日期yyyyMMdd
     */
    private String createDate;
    /**
     * 创建时间HHmmss
     */
    private String createTime;


}
