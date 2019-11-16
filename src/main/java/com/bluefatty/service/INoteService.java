package com.bluefatty.service;

import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-16
 */
public interface INoteService {
    /**
     * 获取主页所有笔记信息
     * @param userId
     * @return
     */
    List getAllNoteInfo(String userId);
}
