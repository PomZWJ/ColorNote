package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.domain.Token;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.INoteKindService;
import com.bluefatty.utils.StringUtils;
import com.bluefatty.utils.SystemConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 笔记分类管理
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Slf4j
@RestController
@RequestMapping(value = "/noteKind")
public class NoteKindController {

    @Autowired
    private INoteKindService noteKindService;


    /**
     * 获取当前用户所有的笔记分类(包含未分类)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAllNoteKindByUserId", method = RequestMethod.POST)
    public ResponseParams getAllNoteKindByUserId(String userId) {
        ResponseParams<List> responseParams = new ResponseParams<List>();
        String desc = "获取用户笔记分类(未包含未分类)";
        log.info("desc = {},userId={}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            List noteKindList = noteKindService.getAllNoteKindByUserId(userId);
            responseParams.setParams(noteKindList);
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
     * 获取当前用户所有的笔记分类(未包含未分类)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAllNoteKindByUserIdWithoutNull", method = RequestMethod.POST)
    public ResponseParams getAllNoteKindByUserIdWithoutNull(String userId) {
        ResponseParams<List> responseParams = new ResponseParams<List>();
        String desc = "获取用户笔记分类(未包含未分类)";
        log.info("desc = {},userId={}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            List noteKindList = noteKindService.getAllNoteKindByUserIdWithoutNull(userId);
            responseParams.setParams(noteKindList);
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
     * 添加笔记分类
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/addNoteKind", method = RequestMethod.POST)
    public ResponseParams addNoteKind(String userId,String noteKindName,String noteKindUrl) {
        ResponseParams<TbNoteKind> responseParams = new ResponseParams<TbNoteKind>();
        String desc = "添加笔记分类";
        log.info("desc = {},userId={},noteKindName={},noteKindUrl={}", desc, userId,noteKindName,noteKindUrl);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteKindName)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_KIND_NAME_IS_NULL.getCode(), MessageCode.ERROR_NOTE_KIND_NAME_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteKindUrl)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_KIND_URL_IS_NULL.getCode(), MessageCode.ERROR_NOTE_KIND_URL_IS_NULL.getMsg());
            }
            TbNoteKind tbNoteKind = noteKindService.addNoteKind(userId, noteKindName, noteKindUrl);
            responseParams.setParams(tbNoteKind);
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
     * 根据笔记类型ID删除笔记分类
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteNoteKindByNoteKindId", method = RequestMethod.POST)
    public ResponseParams deleteNoteKindByNoteKindId(String userId,String noteKindId) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "根据笔记类型ID删除笔记分类";
        log.info("desc = {},userId={},noteKindId={}", desc, userId,noteKindId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteKindId)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_KIND_ID_IS_NULL.getCode(), MessageCode.ERROR_NOTE_KIND_ID_IS_NULL.getMsg());
            }
            noteKindService.deleteNoteKindByNoteKindId(userId,noteKindId);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},删除失败, 原因:{}", desc, e);
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
