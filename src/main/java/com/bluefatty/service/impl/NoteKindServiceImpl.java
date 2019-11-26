package com.bluefatty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.dao.TbNoteMapper;
import com.bluefatty.domain.TbNote;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.service.INoteKindService;
import com.bluefatty.utils.CommonUtils;
import com.bluefatty.utils.DateUtils;
import com.bluefatty.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
@Slf4j
@Service
public class NoteKindServiceImpl implements INoteKindService {
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;
    @Autowired
    private TbNoteMapper tbNoteMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List getAllNoteKindByUserId(String userId) {
        List<TbNoteKind> tbNoteKinds = tbNoteKindMapper.selectAscByUserId(userId);
        List<Map> tnkList = new ArrayList<>();
        Map<String, Object> temp = null;
        JSONObject jsonValue = null;
        temp =  new HashMap<>();
        temp.put("iconUrl", "../../static/bookmark/bookmark-black.png");
        temp.put("markText", "未分类");
        temp.put("id", "");
        jsonValue = new JSONObject();
        jsonValue.put("iconUrl","../../static/bookmark/bookmark-black.png");
        jsonValue.put("markText","未分类");
        jsonValue.put("id", "");
        temp.put("value",jsonValue);
        tnkList.add(temp);
        for (TbNoteKind tnk : tbNoteKinds) {
            temp = new HashMap<>();
            temp.put("iconUrl", "../../static/bookmark/"+tnk.getKindIconUrl());
            temp.put("markText", tnk.getNoteKindName());
            temp.put("id", tnk.getNoteKindId());
            jsonValue = new JSONObject();
            jsonValue.put("iconUrl","../../static/bookmark/"+tnk.getKindIconUrl());
            jsonValue.put("markText",tnk.getNoteKindName());
            jsonValue.put("id", tnk.getNoteKindId());
            temp.put("value",jsonValue);
            tnkList.add(temp);
        }
        return tnkList;
    }

    @Override
    public TbNoteKind addNoteKind(String userId,String noteKindName,String noteKindUrl) {
        TbNoteKind tbNoteKind = new TbNoteKind();
        tbNoteKind.setCreateDate(DateUtils.getCurrentDate());
        tbNoteKind.setCreateTime(DateUtils.getCurrentTime());
        tbNoteKind.setKindIconUrl(noteKindUrl);
        tbNoteKind.setNoteKindName(noteKindName);
        tbNoteKind.setUserId(userId);
        tbNoteKind.setNoteKindId(CommonUtils.getUuid());
        tbNoteKindMapper.insertSelective(tbNoteKind);
        return tbNoteKind;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List getAllNoteKindByUserIdWithoutNull(String userId) {
        List<TbNoteKind> tbNoteKinds = tbNoteKindMapper.selectAscByUserId(userId);
        List<Map> tnkList = new ArrayList<>();
        for (TbNoteKind tnk : tbNoteKinds) {
            Map<String, Object>  temp = new HashMap<>();
            temp.put("iconUrl", tnk.getKindIconUrl());
            temp.put("markText", tnk.getNoteKindName());
            temp.put("id", tnk.getNoteKindId());
            JSONObject jsonValue = new JSONObject();
            jsonValue.put("iconUrl","../../static/bookmark/"+tnk.getKindIconUrl());
            jsonValue.put("markText",tnk.getNoteKindName());
            jsonValue.put("id", tnk.getNoteKindId());
            temp.put("value",jsonValue);
            tnkList.add(temp);
        }
        return tnkList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNoteKindByNoteKindId(String userId,String noteKindId) {
        tbNoteKindMapper.deleteByPrimaryKey(noteKindId);
        //删除分类后，对于所有该分类下的笔记，全部归档为未分类
        TbNote tbNote = new TbNote();
        tbNote.setUserId(userId);
        tbNote.setNoteKindId(noteKindId);
        tbNoteMapper.updateNoteKindToNullByUserIdAndNoteKindId(tbNote);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNoteKindByUserId(List<Map<String,String>>params) {
        /*[{
            "markText": "个人1",
            "iconUrl": "bookmark-blue.png",
            "id": "9949bffeea84460d989ce2d2356316e4"
        }]*/
        for(int i=0;i<params.size();i++){
            Map<String,String> map = params.get(i);
            TbNoteKind tbNoteKind = new TbNoteKind();
            tbNoteKind.setNoteKindId(map.get("id"));
            tbNoteKind.setNoteKindName(map.get("markText"));
            String newIconUrl = map.get("newIconUrl");
            if(StringUtils.isEmpty(newIconUrl)){
                tbNoteKind.setKindIconUrl(map.get("iconUrl"));
            }else{
                tbNoteKind.setKindIconUrl(newIconUrl);
            }
            tbNoteKindMapper.updateByPrimaryKeySelective(tbNoteKind);
        }
    }

}
