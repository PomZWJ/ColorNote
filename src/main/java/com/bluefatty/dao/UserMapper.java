package com.bluefatty.dao;

import com.bluefatty.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户操作Dao
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public interface UserMapper {
    /**
     * 根据主键删除
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(String userId);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据主键查询
     * @param userId
     * @return
     */
    User selectByPrimaryKey(String userId);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);
}