package com.bluefatty.controller;

import com.bluefatty.runner.TbSystemConfigConfigRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private TbSystemConfigConfigRunner tbSystemConfigConfigRunner;

    @RequestMapping(value = "/get" , method = RequestMethod.POST)
    public String get(){
        String imageUrl = tbSystemConfigConfigRunner.getConfigValue("imageUrl");
        return imageUrl;
    }
}
