package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.constants.FavoriteFiled;
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
     * 获取主页所有笔记，需要排序
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

    /**
     * 添加一条笔记
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/addUserNoteInfo", method = RequestMethod.POST)
    public ResponseParams addUserNoteInfo(String userId,String noteKindId,String noteContent,String noteTime) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "添加一条笔记";
        log.info("desc = {},userId={}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteContent)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_CONTENT_IS_NULL.getCode(), MessageCode.ERROR_NOTE_CONTENT_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteTime)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_TIME_IS_NULL.getCode(), MessageCode.ERROR_NOTE_TIME_IS_NULL.getMsg());
            }
            noteService.addUserNoteInfo(userId,noteKindId,noteContent,noteTime);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},添加失败, 原因:{}", desc, e);
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
     * 获取一条笔记
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserNoteByNoteId", method = RequestMethod.POST)
    public ResponseParams getUserNoteByNoteId(String userId,String noteId) {
        ResponseParams<Map> responseParams = new ResponseParams<Map>();
        String desc = "获取一条笔记";
        log.info("desc = {},userId={},noteId = {}", desc, userId,noteId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteId)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_ID_IS_NULL.getCode(), MessageCode.ERROR_NOTE_ID_IS_NULL.getMsg());
            }
            Map returnMap = noteService.getUserNoteByUserId(userId, noteId);
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
     * 更新笔记收藏状态
     *
     * @param userId
     * @param noteId
     * @param favState
     * @return
     */
    @RequestMapping(value = "/updateNoteFavState", method = RequestMethod.POST)
    public ResponseParams updateNoteFavState(String userId,String noteId,String favState) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "更新笔记收藏状态";
        log.info("desc = {},userId={},noteId = {},favState={}", desc, userId,noteId,favState);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteId)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_ID_IS_NULL.getCode(), MessageCode.ERROR_NOTE_ID_IS_NULL.getMsg());
            }
            if(StringUtils.isEmpty(favState)){
                throw new ColorNoteException(MessageCode.ERROR_FAV_STATE_IS_NULL.getCode(), MessageCode.ERROR_FAV_STATE_IS_NULL.getMsg());
            }
            noteService.updateNoteFavState(userId, noteId,favState);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},更新失败, 原因:{}", desc, e);
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
     * 删除笔记到垃圾箱
     *
     * @param userId
     * @param noteId
     * @return
     */
    @RequestMapping(value = "/deleteNoteToRubbishByUserIdAndNoteId", method = RequestMethod.POST)
    public ResponseParams deleteNoteToRubbishByUserIdAndNoteId(String userId,String noteId) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "删除笔记到垃圾箱";
        log.info("desc = {},userId={},noteId = {}", desc, userId,noteId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteId)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_ID_IS_NULL.getCode(), MessageCode.ERROR_NOTE_ID_IS_NULL.getMsg());
            }
            noteService.deleteNoteToRubbishByUserIdAndNoteId(userId, noteId);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},更新失败, 原因:{}", desc, e);
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
     * 更新一条笔记
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateUserNoteInfo", method = RequestMethod.POST)
    public ResponseParams updateUserNoteInfo(String userId,String noteId,String noteKindId,String noteContent,String noteTime) {
        ResponseParams<Boolean> responseParams = new ResponseParams<Boolean>();
        String desc = "更新一条笔记";
        log.info("desc = {},userId={}", desc, userId);
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new ColorNoteException(MessageCode.ERROR_USERID_IS_NULL.getCode(), MessageCode.ERROR_USERID_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteContent)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_CONTENT_IS_NULL.getCode(), MessageCode.ERROR_NOTE_CONTENT_IS_NULL.getMsg());
            }
            if (StringUtils.isEmpty(noteTime)) {
                throw new ColorNoteException(MessageCode.ERROR_NOTE_TIME_IS_NULL.getCode(), MessageCode.ERROR_NOTE_TIME_IS_NULL.getMsg());
            }
            noteService.updateUserNoteInfo(userId,noteId,noteKindId,noteContent,noteTime);
            responseParams.setParams(true);
        } catch (Exception e) {
            log.error("desc={},添加失败, 原因:{}", desc, e);
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
