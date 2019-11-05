package com.bluefatty.dao;

import com.bluefatty.domain.TbNoteKind;

public interface TbNoteKindMapper {

    int deleteByPrimaryKey(String noteKindId);


    int insert(TbNoteKind record);


    int insertSelective(TbNoteKind record);


    TbNoteKind selectByPrimaryKey(String noteKindId);


    int updateByPrimaryKeySelective(TbNoteKind record);


    int updateByPrimaryKey(TbNoteKind record);
}