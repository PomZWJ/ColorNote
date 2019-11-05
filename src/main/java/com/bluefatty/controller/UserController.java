package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.constants.AccountStatusFiled;
import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.domain.Token;
import com.bluefatty.domain.TbUser;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.TbUserService;
import com.bluefatty.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.POST)
    public ResponseParams<Boolean> sendVerificationCode(String userId) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        responseParams.setParams(true);
        String desc = "发送登录验证码";
        log.info("desc = {} , userId = {}", desc, userId);
        try {
            String code = "123456";
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            emailUtils.sendEmail(userId, code);
            redisUtils.cacheString(RedisKeyPrefix.VERIFICATION_CODE + userId, Md5Utils.getMd5Str(code), 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("desc={},获取失败,原因:{}", desc, e);
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
        log.info("desc = {} , 出参 = {} ", desc, JSON.toJSONString(responseParams));
        return responseParams;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseParams login(String userId, String verificationCode) {
        ResponseParams<Map> responseParams = new ResponseParams<Map>();
        String desc = "用户登录";
        Map<String,String> returnMap = new HashMap<>();
        log.info("desc = {} , userId = {},verificationCode={}", desc, userId, verificationCode);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(verificationCode)) {
                throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getCode(), MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getMsg());
            }
            //验证验证码是否通过
            String userVerificationCode = redisUtils.getCacheString(RedisKeyPrefix.VERIFICATION_CODE + userId);
            if (!Md5Utils.getMd5Str(verificationCode).equals(StringUtils.getValue(userVerificationCode))) {
                throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_EXIST.getMsg());
            }
            //查询是否已经存在token，如果已存在直接返回，不存在在重新生成
            String existToken = redisUtils.getCacheString(RedisKeyPrefix.USER_TOKEN+userId);
            if(!StringUtils.isEmpty(existToken)){
                returnMap.put("userId",userId);
                returnMap.put("token",existToken);
            }else {
                TbUser tbUser = tbUserService.selectByPrimaryKey(userId);
                if (tbUser == null) {
                    //自动建立账户
                    tbUser = new TbUser();
                    tbUser.setAccountStatus(AccountStatusFiled.NORMAL);
                    tbUser.setCreateDate(DateUtils.getCurrentDate());
                    tbUser.setCreateTime(DateUtils.getCurrentTime());
                    tbUser.setUserId(userId);
                    tbUser.setUserName("cn_" + userId);
                    tbUserService.insert(tbUser);
                }
                Token token = new Token();
                token.setCreateDate(DateUtils.getCurrentDate());
                token.setCreateTime(DateUtils.getCurrentTime());
                token.setUser(tbUser);
                token.setSign(Md5Utils.getMd5Str(JSON.toJSONString(tbUser)));
                String tokenStr = RsaUtils.rsaPublicEncrypt(JSON.toJSONString(token));
                redisUtils.cacheString(RedisKeyPrefix.USER_TOKEN + userId, tokenStr, 72, TimeUnit.HOURS);
                returnMap.put("userId", userId);
                returnMap.put("token", existToken);
            }
            responseParams.setParams(returnMap);
        } catch (Exception e) {
            log.error("desc={},登录失败, 原因:{}", desc, e);
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
        log.info("desc = {} , 出参 = {} ", desc, JSON.toJSONString(responseParams));
        return responseParams;
    }

    @RequestMapping(value = "/getCurrentLoginUserInfo", method = RequestMethod.POST)
    public ResponseParams getCurrentLoginUserInfo(String userId, String token) {
        ResponseParams<Token> responseParams = new ResponseParams<Token>();
        String desc = "用户信息获取";
        Map<String,String> returnMap = new HashMap<>();
        log.info("desc = {},userId={},token={}",desc,userId,token);
        try{
            String cacheString = redisUtils.getCacheString(RedisKeyPrefix.USER_TOKEN + userId);
            if(StringUtils.isEmpty(cacheString)){
                throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getCode(),MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getMsg());
            }if (cacheString.equals(token)) {
                String tokenStr1 = RsaUtils.rsaPrivateKeyDecrypt(token);
                String tokenStr2 = RsaUtils.rsaPrivateKeyDecrypt(cacheString);
                Token token1 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr1), Token.class);
                Token token2 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr2), Token.class);

                if (!token1.getSign().equals(token2.getSign())) {
                    throw new ColorNoteException(MessageCode.ERROR_CHECK_SIGN.getCode(),MessageCode.ERROR_CHECK_SIGN.getMsg());
                } else {
                    responseParams.setParams(JSON.parseObject(tokenStr2, new TypeReference<Token>() {}));
                }
            } else {
                throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NOT_OK.getCode(),MessageCode.ERROR_TOKEN_IS_NOT_OK.getMsg());
            }
        }catch (Exception e){
            log.error("desc={},获取失败, 原因:{}", desc, e);
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
        log.info("desc = {} , 出参 = {} ", desc, JSON.toJSONString(responseParams));
        return responseParams;
    }
}
