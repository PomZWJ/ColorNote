package com.bluefatty.service.impl;

import com.bluefatty.dao.TbUserMapper;
import com.bluefatty.domain.TbUser;
import com.bluefatty.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Service
public class UserServiceImpl implements TbUserService {
    @Autowired
    private TbUserMapper tbUserMapper;


    @Override
    public int deleteByPrimaryKey(String userId) {
        return tbUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(TbUser record) {
        return tbUserMapper.insert(record);
    }

    @Override
    public int insertSelective(TbUser record) {
        return tbUserMapper.insertSelective(record);
    }

    @Override
    public TbUser selectByPrimaryKey(String userId) {
        return tbUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(TbUser record) {
        return tbUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TbUser record) {
        return tbUserMapper.updateByPrimaryKey(record);
    }
}
