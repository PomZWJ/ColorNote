package com.bluefatty.controller;

import com.alibaba.fastjson.JSON;
import com.bluefatty.common.ResponseParams;
import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.domain.Token;
import com.bluefatty.exception.ColorNoteException;
import com.bluefatty.exception.MessageCode;
import com.bluefatty.service.INoteKindService;
import com.bluefatty.utils.StringUtils;
import com.bluefatty.utils.SystemConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 笔记分类管理
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Slf4j
@RestController
@RequestMapping(value = "/noteKind")
public class NoteKindController {

    @Autowired
    private SystemConfigUtils systemConfigUtils;
    @Autowired
    private INoteKindService noteKindService;


}
