package com.bluefatty.dao;

import com.bluefatty.domain.TbSystemConfig;

import java.util.List;

public interface TbSystemConfigMapper {

    int deleteByPrimaryKey(String configId);


    int insert(TbSystemConfig record);

    int insertSelective(TbSystemConfig record);


    TbSystemConfig selectByPrimaryKey(String configId);


    int updateByPrimaryKeySelective(TbSystemConfig record);


    int updateByPrimaryKey(TbSystemConfig record);

    List<TbSystemConfig> selectAll();
}