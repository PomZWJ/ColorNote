package com.bluefatty.service.impl;

import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.service.TbNoteKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Service
public class TbNoteKindServiceImpl implements TbNoteKindService {
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;
    @Override
    public int deleteByPrimaryKey(String noteKindId) {
        return tbNoteKindMapper.deleteByPrimaryKey(noteKindId);
    }

    @Override
    public int insert(TbNoteKind record) {
        return tbNoteKindMapper.insert(record);
    }

    @Override
    public int insertSelective(TbNoteKind record) {
        return tbNoteKindMapper.insertSelective(record);
    }

    @Override
    public TbNoteKind selectByPrimaryKey(String noteKindId) {
        return tbNoteKindMapper.selectByPrimaryKey(noteKindId);
    }

    @Override
    public int updateByPrimaryKeySelective(TbNoteKind record) {
        return tbNoteKindMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TbNoteKind record) {
        return tbNoteKindMapper.updateByPrimaryKey(record);
    }

    @Override
    public int batchInsert(List<TbNoteKind> list) {
        return tbNoteKindMapper.batchInsert(list);
    }
}
