package com.bluefatty.facade;

import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.utils.CommonUtils;
import com.bluefatty.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Component
public class NoteKindFacade {
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;

    public void userInitInsertKind(String userId){
        List<TbNoteKind> list = new LinkedList<>();
        String[] name = {"个人","生活","工作","旅游"};
        String[] iconUrl = {"bookmark-personal.png","bookmark-life.png","bookmark-work.png","bookmark-tourism.png"};
        for(int i=0;i<4;i++){
            TbNoteKind tbNoteKind = new TbNoteKind();
            tbNoteKind.setNoteKindName(name[i]);
            tbNoteKind.setNoteKindId(CommonUtils.getUuid());
            tbNoteKind.setUserId(userId);
            tbNoteKind.setCreateDate(DateUtils.getCurrentDate());
            tbNoteKind.setCreateTime(DateUtils.getCurrentTime());
            tbNoteKind.setKindIconUrl(iconUrl[i]);
            list.add(tbNoteKind);
        }
        tbNoteKindMapper.batchInsert(list);
    }
}
