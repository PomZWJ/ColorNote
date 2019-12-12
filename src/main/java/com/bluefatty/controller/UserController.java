package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.domain.Token;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.IUserService;
import com.bluefatty.utils.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


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
    private IUserService userService;

    /**
     * 发送用户验证码
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.POST)
    public ResponseParams<Boolean> sendVerificationCode(String userId) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        responseParams.setParams(true);
        String desc = "发送登录验证码";
        log.info("desc = {} , userId = {}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            userService.sendEmailVerificationCode(userId);
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

    /**
     * 用户登录，如果数据库中没有用户记录，则自动开户
     *
     * @param userId
     * @param verificationCode
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseParams login(String userId, String verificationCode) {
        ResponseParams<Map> responseParams = new ResponseParams<Map>();
        String desc = "用户登录";
        log.info("desc = {} , userId = {},verificationCode={}", desc, userId, verificationCode);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(verificationCode)) {
                throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getCode(), MessageCode.ERROR_VERIFICATION_CODE_IS_NULL.getMsg());
            }
            Map<String, String> map = userService.userLogin(userId, verificationCode);
            responseParams.setParams(map);
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

    /**
     * 获取用户信息
     *
     * @param userId
     * @param token
     * @return
     */
    @RequestMapping(value = "/getCurrentLoginUserInfo", method = RequestMethod.POST)
    public ResponseParams getCurrentLoginUserInfo(String userId, String token) {
        ResponseParams<Token> responseParams = new ResponseParams<Token>();
        String desc = "用户信息获取";
        log.info("desc = {},userId={},token={}", desc, userId, token);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(token)) {
                throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NULL.getCode(), MessageCode.ERROR_TOKEN_IS_NULL.getMsg());
            }
            Token currentLoginUserInfo = userService.getCurrentLoginUserInfo(userId, token);
            responseParams.setParams(currentLoginUserInfo);
        } catch (Exception e) {
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

    /**
     * 获取用户主页信息
     *
     * @param userId
     * @param token
     * @return
     */
    @RequestMapping(value = "/getUserIndexInfo", method = RequestMethod.POST)
    public ResponseParams getUserIndexInfo(String userId, String token) {
        ResponseParams<Map> responseParams = new ResponseParams<Map>();
        String desc = "获取用户主页信息";
        log.info("desc = {},userId={},token={}", desc, userId, token);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            /*if (StringUtils.isEmpty(token)) {
                throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NULL.getCode(), MessageCode.ERROR_TOKEN_IS_NULL.getMsg());
            }*/
            Map<String, Object> returnMap = userService.getUserIndexInfo(userId);
            responseParams.setParams(returnMap);
        } catch (Exception e) {
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



    /**
     * 判断用户token是否正确
     *
     * @param userId
     * @param token
     * @return
     */
    @RequestMapping(value = "/determineUserTokenIsCorrect", method = RequestMethod.POST)
    public ResponseParams determineUserTokenIsCorrect(String userId, String token) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "判断用户token是否正确";
        log.info("desc = {},userId={},token={}", desc, userId, token);
        try {
            if (StringUtils.isEmpty(userId)) {
                responseParams.setParams(false);
            }
            if (StringUtils.isEmpty(token)) {
                responseParams.setParams(false);
            }
            if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(token)){
                try {
                    responseParams.setParams(userService.determineUserTokenIsCorrect(userId,token));
                }catch (Exception e){
                    log.error("对比token失败,e={}",e);
                    responseParams.setParams(false);
                }


            }

        } catch (Exception e) {
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


    /**
     * 体验用户登录，如果数据库中没有用户记录，则自动开户
     *
     * @return
     */
    @RequestMapping(value = "/tryAccountLogin", method = RequestMethod.POST)
    public ResponseParams tryAccountLogin() {
        ResponseParams<Map> responseParams = new ResponseParams<Map>();
        String desc = "体验账户用户登录";
        log.info("desc = {} ");
        try {
            Map<String, String> map = userService.tryUserLogin();
            responseParams.setParams(map);
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
}
