package com.bluefatty.service;

import com.bluefatty.domain.TbNoteKind;

import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
public interface TbNoteKindService {
    int deleteByPrimaryKey(String noteKindId);


    int insert(TbNoteKind record);


    int insertSelective(TbNoteKind record);


    TbNoteKind selectByPrimaryKey(String noteKindId);


    int updateByPrimaryKeySelective(TbNoteKind record);


    int updateByPrimaryKey(TbNoteKind record);

    int batchInsert(List<TbNoteKind> list);
}
