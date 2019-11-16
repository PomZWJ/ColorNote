package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.INoteService;
import com.bluefatty.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 笔记分类管理
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-16
 */
@Slf4j
@RestController
@RequestMapping(value = "/note")
public class NoteController {

    @Autowired
    private INoteService noteService;
    /**
     * 获取主页所有笔记
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAllNoteInfo", method = RequestMethod.POST)
    public ResponseParams getAllNoteInfo(String userId) {
        ResponseParams<List> responseParams = new ResponseParams<List>();
        String desc = "获取主页所有笔记";
        log.info("desc = {},userId={}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            responseParams.setParams(noteService.getAllNoteInfo(userId));
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
}
