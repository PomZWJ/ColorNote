package com.bluefatty.service;


import java.util.List;
import java.util.Map;

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
    List getAllNoteInfo(String userId,String noteKindId,String noteContent,String isFav);

    void addUserNoteInfo(String userId,String noteKindId,String noteContent,String noteTime);

    Map getUserNoteByUserId(String userId, String noteId);

    void updateNoteFavState(String userId,String noteId,String favState);

    void deleteNoteToRubbishByUserIdAndNoteId(String userId, String noteId);

    void updateUserNoteInfo(String userId,String noteId,String noteKindId,String noteContent,String noteTime);

}
