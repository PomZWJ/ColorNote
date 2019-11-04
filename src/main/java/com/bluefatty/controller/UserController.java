package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.domain.Token;
import com.bluefatty.domain.TbUser;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.TbUserService;
import com.bluefatty.utils.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
public class UserController {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TbUserService tbUserService;
    @Autowired
    private EmailUtils emailUtils;

    @RequestMapping(value = "sendVerificationCode",method = RequestMethod.POST)
    public ResponseParams<Boolean> sendVerificationCode(String userId){
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        responseParams.setParams(true);
        String desc = "发送登录验证码";
        log.info("desc = {} , userId = {}",desc, userId);
        try {
            if(StringUtils.isEmpty(userId)){
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(),MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            emailUtils.sendEmail(userId);
        }catch (Exception e){
            log.error("desc={},获取失败, 类和方法名={},原因:{}", desc, CommonUtils.getCurrentClassAndMethod(), e);
            //清空赋值
            responseParams.setParams(null);
            if (e instanceof ColorNoteException) {
                ColorNoteException ce = (ColorNoteException) e;
                responseParams.setResultCode(ce.getErrorCode());
                responseParams.setResultMsg(ce.getErrorMessage());
            } else {
                responseParams.setResultCode(MessageCode.ERROR_UNKOWN.getCode());
                responseParams.setResultMsg(MessageCode.ERROR_UNKOWN.getMsg() + "," + e.getMessage());
            }
        }
        log.info("desc = {} , 出参 = {} ",desc, JSON.toJSONString(responseParams));
        return responseParams;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseParams login(String userId, String verificationCode) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        responseParams.setParams(true);
        String desc = "用户登录";
        log.info("desc = {} , userId = {},verificationCode={}",desc, userId,verificationCode);
        try {
            if(StringUtils.isEmpty(userId)){
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(),MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if(StringUtils.isEmpty(verificationCode)){
                throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getCode(),MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getMsg());
            }
            //删除当前存在的userId登录信息
            redisUtils.deleteCache(userId);
            //验证验证码是否通过
            TbUser tbUser = tbUserService.selectByPrimaryKey(userId);
            if(tbUser == null){
                //自动建立账户

            }else{
                Token token = new Token();
                token.setCreateDate(DateUtils.getCurrentDate());
                token.setCreateTime(DateUtils.getCurrentTime());
                token.setUser(tbUser);
                token.setSign(Md5Utils.getMd5Str(JSON.toJSONString(tbUser)));
                String tokenStr = RsaUtils.rsaPublicEncrypt(JSON.toJSONString(token));
                redisUtils.cacheString(RedisKeyPrefix.USER_TOKEN + userId, tokenStr, 3600, TimeUnit.SECONDS);
            }
            responseParams.setParams(true);
        }catch (Exception e){
            log.error("desc={},登录失败, 类和方法名={},原因:{}", desc, CommonUtils.getCurrentClassAndMethod(), e);
            //清空赋值
            responseParams.setParams(null);
            if (e instanceof ColorNoteException) {
                ColorNoteException ce = (ColorNoteException) e;
                responseParams.setResultCode(ce.getErrorCode());
                responseParams.setResultMsg(ce.getErrorMessage());
            } else {
                responseParams.setResultCode(MessageCode.ERROR_UNKOWN.getCode());
                responseParams.setResultMsg(MessageCode.ERROR_UNKOWN.getMsg() + "," + e.getMessage());
            }
        }
        log.info("desc = {} , 出参 = {} ",desc, JSON.toJSONString(responseParams));
        return responseParams;
    }

    @RequestMapping(value = "/getCurrentLoginUserInfo", method = RequestMethod.POST)
    public Object getCurrentLoginUserInfo(String userId, String token) {
        String cacheString = redisUtils.getCacheString(RedisKeyPrefix.USER_TOKEN + userId);
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
}
