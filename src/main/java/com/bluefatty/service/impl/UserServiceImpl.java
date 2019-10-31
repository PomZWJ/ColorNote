package com.bluefatty.service.impl;

import com.bluefatty.dao.UserMapper;
import com.bluefatty.domain.CnUser;
import com.bluefatty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public int deleteByPrimaryKey(String userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(CnUser record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(CnUser record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public CnUser selectByPrimaryKey(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(CnUser record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CnUser record) {
        return userMapper.updateByPrimaryKey(record);
    }
}
