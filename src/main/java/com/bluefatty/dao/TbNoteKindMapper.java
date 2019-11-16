package com.bluefatty.dao;

import com.bluefatty.domain.TbNoteKind;

import java.util.List;

public interface TbNoteKindMapper {

    int deleteByPrimaryKey(String noteKindId);


    int insert(TbNoteKind record);


    int insertSelective(TbNoteKind record);


    TbNoteKind selectByPrimaryKey(String noteKindId);


    int updateByPrimaryKeySelective(TbNoteKind record);


    int updateByPrimaryKey(TbNoteKind record);

    int batchInsert(List<TbNoteKind> list);

    List<TbNoteKind>selectAscByUserId(String userId);
}