package com.bluefatty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.service.INoteKindService;
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
}
