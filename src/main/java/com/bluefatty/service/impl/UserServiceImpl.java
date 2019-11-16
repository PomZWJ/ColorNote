package com.bluefatty.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bluefatty.constants.AccountStatusFiled;
import com.bluefatty.constants.FavoriteFiled;
import com.bluefatty.constants.NoteStatusFiled;
import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.dao.TbNoteMapper;
import com.bluefatty.dao.TbUserMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.domain.TbUser;
import com.bluefatty.domain.Token;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.INoteKindService;
import com.bluefatty.service.IUserService;
import com.bluefatty.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;
    @Autowired
    private TbNoteMapper tbNoteMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendEmailVerificationCode(String userId) {
        String code = PhoneCodeUtils.getVerifyCode();
        emailUtils.sendEmail(userId, code);
        redisUtils.cacheString(RedisKeyPrefix.VERIFICATION_CODE + userId, Md5Utils.getMd5Str(code), 60, TimeUnit.SECONDS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> userLogin(String userId, String verificationCode) {
        Map<String, String> returnMap = new HashMap<>();
        //验证验证码是否通过
        String userVerificationCode = redisUtils.getCacheString(RedisKeyPrefix.VERIFICATION_CODE + userId);
        if (StringUtils.isEmpty(userVerificationCode)) {
            throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_EXIST.getCode(), MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_EXIST.getMsg());
        }
        if (!Md5Utils.getMd5Str(verificationCode).equals(StringUtils.getValue(userVerificationCode))) {
            throw new ColorNoteException(MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_OK.getCode(), MessageCode.ERROR_VERIFICATION_CODE_IS_NOT_OK.getMsg());
        }
        //查询是否已经存在token，如果已存在直接返回，不存在在重新生成
        String existToken = redisUtils.getCacheString(RedisKeyPrefix.USER_TOKEN + userId);
        if (!StringUtils.isEmpty(existToken)) {
            returnMap.put("userId", userId);
            returnMap.put("token", existToken);
        } else {
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(userId);
            if (tbUser == null) {
                log.info("用户ID={}不存在，自动开户", userId);
                //自动建立账户
                tbUser = new TbUser();
                tbUser.setAccountStatus(AccountStatusFiled.NORMAL);
                tbUser.setCreateDate(DateUtils.getCurrentDate());
                tbUser.setCreateTime(DateUtils.getCurrentTime());
                tbUser.setUserId(userId);
                tbUser.setUserName("cn_" + userId);
                tbUserMapper.insert(tbUser);
                //自动创默认分类
                List<TbNoteKind> list = new LinkedList<>();
                String[] name = {"个人", "生活", "工作", "旅游"};
                String[] iconUrl = {"bookmark-personal.png", "bookmark-life.png", "bookmark-work.png", "bookmark-tourism.png"};
                Date date = new Date();
                for (int i = 0; i < name.length; i++) {
                    TbNoteKind tbNoteKind = new TbNoteKind();
                    tbNoteKind.setNoteKindName(name[i]);
                    tbNoteKind.setNoteKindId(CommonUtils.getUuid());
                    tbNoteKind.setUserId(userId);
                    tbNoteKind.setCreateDate(DateUtils.getCurrentDate());
                    /*靠下一秒区分时间，好排序*/
                    date.setTime(date.getTime() + 1000);
                    tbNoteKind.setCreateTime(DateUtils.getTimeByDate(date));
                    tbNoteKind.setKindIconUrl(iconUrl[i]);
                    list.add(tbNoteKind);
                }
                tbNoteKindMapper.batchInsert(list);
            }
            Token token = new Token();
            token.setCreateDate(DateUtils.getCurrentDate());
            token.setCreateTime(DateUtils.getCurrentTime());
            token.setUser(tbUser);
            token.setSign(Md5Utils.getMd5Str(JSON.toJSONString(tbUser)));
            String tokenStr = RsaUtils.rsaPublicEncrypt(JSON.toJSONString(token));
            redisUtils.cacheString(RedisKeyPrefix.USER_TOKEN + userId, tokenStr, 72, TimeUnit.HOURS);
            returnMap.put("userId", userId);
            returnMap.put("token", tokenStr);
        }
        return returnMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Token getCurrentLoginUserInfo(String userId, String token) {
        String cacheString = redisUtils.getCacheString(RedisKeyPrefix.USER_TOKEN + userId);
        if (StringUtils.isEmpty(cacheString)) {
            throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getCode(), MessageCode.ERROR_TOKEN_IS_NOT_EXIST.getMsg());
        }
        if (cacheString.equals(token)) {
            String tokenStr1 = RsaUtils.rsaPrivateKeyDecrypt(token);
            String tokenStr2 = RsaUtils.rsaPrivateKeyDecrypt(cacheString);
            Token token1 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr1), Token.class);
            Token token2 = JSON.toJavaObject((JSONObject) JSON.parse(tokenStr2), Token.class);

            if (!token1.getSign().equals(token2.getSign())) {
                throw new ColorNoteException(MessageCode.ERROR_CHECK_SIGN.getCode(), MessageCode.ERROR_CHECK_SIGN.getMsg());
            } else {
                Token returnToken = JSON.parseObject(tokenStr2, new TypeReference<Token>() {
                });
                return returnToken;
            }
        } else {
            throw new ColorNoteException(MessageCode.ERROR_TOKEN_IS_NOT_OK.getCode(), MessageCode.ERROR_TOKEN_IS_NOT_OK.getMsg());
        }
    }

    @Override
    public Map getUserIndexInfo(String userId) {
        Map<String, String> dbQueryParams = new HashMap<>();
        dbQueryParams.put("userId",userId);
        //统计各个分类的具体笔记数目
        List<TbNoteKind> tbNoteKinds = tbNoteKindMapper.selectAscByUserId(userId);
        List<Map> tnkList = new ArrayList<>();
        for (TbNoteKind tnk : tbNoteKinds) {
            dbQueryParams.put("noteKindId",tnk.getNoteKindId());
            dbQueryParams.put("isDelete", NoteStatusFiled.NO_DELETE);
            Integer num = tbNoteMapper.statisticsNumberBySelective(dbQueryParams);
            Map<String, Object> temp = new HashMap<>();
            temp.put("iconUrl", "../../static/bookmark/" + tnk.getKindIconUrl());
            temp.put("markText", tnk.getNoteKindName());
            temp.put("id", tnk.getNoteKindId());
            temp.put("markNum", num);
            tnkList.add(temp);
        }
        //所有笔记
        dbQueryParams.put("noteKindId","");
        Integer allNoteNum = tbNoteMapper.statisticsNumberBySelective(dbQueryParams);
        //我的收藏
        dbQueryParams.put("isFav", FavoriteFiled.FAV);
        Integer favNoteNum = tbNoteMapper.statisticsNumberBySelective(dbQueryParams);
        //回收站
        dbQueryParams.clear();
        dbQueryParams.put("isDelete", NoteStatusFiled.RUBBISH);
        Integer rubbishNoteNum = tbNoteMapper.statisticsNumberBySelective(dbQueryParams);
        //设置返回值
        Map<String, Object> returnMap = new HashMap<>(4);
        returnMap.put("noteKind", tnkList);

        returnMap.put("allNote", allNoteNum);
        returnMap.put("myFav", favNoteNum);
        returnMap.put("nearDel", rubbishNoteNum);
        return returnMap;
    }
}
