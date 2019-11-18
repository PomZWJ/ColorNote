package com.bluefatty.service.impl;

import com.bluefatty.constants.FavoriteFiled;
import com.bluefatty.constants.NoteStatusFiled;
import com.bluefatty.dao.TbNoteMapper;
import com.bluefatty.domain.TbNote;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List getAllNoteInfo(String userId) {
        Map<String,String>dbQueryParams = new HashMap<>();
        dbQueryParams.put("userId",userId);
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
}
