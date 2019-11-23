package com.bluefatty.service.impl;

import com.bluefatty.constants.FavoriteFiled;
import com.bluefatty.constants.NoteStatusFiled;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.dao.TbNoteMapper;
import com.bluefatty.domain.TbNote;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.INoteService;
import com.bluefatty.utils.CommonUtils;
import com.bluefatty.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-16
 */
@Slf4j
@Service
public class NoteServiceImpl implements INoteService {
    @Autowired
    private TbNoteMapper tbNoteMapper;
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List getAllNoteInfo(String userId) {
        Map<String,String>dbQueryParams = new HashMap<>();
        dbQueryParams.put("userId",userId);
        dbQueryParams.put("isDelete",NoteStatusFiled.NO_DELETE);
        List<TbNote> tbNotes = tbNoteMapper.selectBySelective(dbQueryParams);
        return tbNotes;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserNoteInfo(String userId,String noteKindId,String noteContent,String noteTime) {
        TbNote tbNote = new TbNote();
        tbNote.setNoteId(CommonUtils.getUuid());
        tbNote.setNoteKindId(noteKindId);
        tbNote.setUserId(userId);
        tbNote.setCreateDate(DateUtils.getCurrentDate());
        tbNote.setCreateTime(DateUtils.getCurrentTime());
        tbNote.setNoteContent(noteContent);
        tbNote.setIsFav(FavoriteFiled.NO_FAV);
        tbNote.setIsDelete(NoteStatusFiled.NO_DELETE);
        tbNote.setNoteTime(noteTime);
        tbNoteMapper.insertSelective(tbNote);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map getUserNoteByUserId(String userId, String noteId) {
        Map<String,String>returnMap = new HashMap<>();
        Map<String,String>dbQueryMap = new HashMap<>();
        dbQueryMap.put("userId",userId);
        dbQueryMap.put("noteId",noteId);
        TbNote tbNote= tbNoteMapper.selectOneByUserIdAndNoteId(dbQueryMap);
        if(tbNote==null){
            throw new ColorNoteException(MessageCode.ERROR_NOTE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_NOTE_IS_NOT_EXIST.getMsg());
        }
        //还需要查找一下笔记类型的具体信息
        TbNoteKind tbNoteKind = tbNoteKindMapper.selectByPrimaryKey(tbNote.getNoteKindId());
        returnMap.put("noteContent",tbNote.getNoteContent());
        returnMap.put("noteTime",tbNote.getNoteTime());
        returnMap.put("isFav",tbNote.getIsFav());
        returnMap.put("noteKindId",tbNote.getNoteKindId());
        if(tbNoteKind!=null){
            returnMap.put("kindIconUrl",tbNoteKind.getKindIconUrl());
            returnMap.put("noteKindName",tbNoteKind.getNoteKindName());
        }
        return returnMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNoteFavState(String userId, String noteId, String favState) {
        Map<String,String>dbQueryMap = new HashMap<>();
        dbQueryMap.put("userId",userId);
        dbQueryMap.put("noteId",noteId);
        TbNote tbNote= tbNoteMapper.selectOneByUserIdAndNoteId(dbQueryMap);
        if(tbNote==null){
            throw new ColorNoteException(MessageCode.ERROR_NOTE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_NOTE_IS_NOT_EXIST.getMsg());
        }
        TbNote updateEntity = new TbNote();
        updateEntity.setNoteId(noteId);
        updateEntity.setIsFav(favState);
        tbNoteMapper.updateByPrimaryKeySelective(updateEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNoteToRubbishByUserIdAndNoteId(String userId, String noteId) {
        Map<String,String>dbQueryMap = new HashMap<>();
        dbQueryMap.put("userId",userId);
        dbQueryMap.put("noteId",noteId);
        dbQueryMap.put("isDelete",NoteStatusFiled.NO_DELETE);
        TbNote tbNote= tbNoteMapper.selectOneByUserIdAndNoteId(dbQueryMap);
        if(tbNote==null){
            throw new ColorNoteException(MessageCode.ERROR_NOTE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_NOTE_IS_NOT_EXIST.getMsg());
        }
        TbNote updateEntity = new TbNote();
        updateEntity.setNoteId(noteId);
        updateEntity.setIsDelete(NoteStatusFiled.RUBBISH);
        updateEntity.setDeleteDate(DateUtils.getCurrentDate());
        tbNoteMapper.updateByPrimaryKeySelective(updateEntity);
    }

    @Override
    public void updateUserNoteInfo(String userId, String noteId,String noteKindId, String noteContent, String noteTime) {
        Map<String,String>dbQueryMap = new HashMap<>();
        dbQueryMap.put("userId",userId);
        dbQueryMap.put("noteId",noteId);
        dbQueryMap.put("isDelete",NoteStatusFiled.NO_DELETE);
        TbNote tbNote= tbNoteMapper.selectOneByUserIdAndNoteId(dbQueryMap);
        if(tbNote==null){
            throw new ColorNoteException(MessageCode.ERROR_NOTE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_NOTE_IS_NOT_EXIST.getMsg());
        }
        TbNote updateEntity = new TbNote();
        updateEntity.setNoteId(noteId);
        updateEntity.setNoteKindId(noteKindId);
        updateEntity.setNoteContent(noteContent);
        updateEntity.setNoteTime(noteTime);
        tbNoteMapper.updateByPrimaryKeySelective(updateEntity);
    }
}
