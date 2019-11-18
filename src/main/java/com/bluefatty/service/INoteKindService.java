package com.bluefatty.service;

import com.bluefatty.domain.TbNoteKind;

import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
public interface INoteKindService {
    List getAllNoteKindByUserId(String userId);
}
