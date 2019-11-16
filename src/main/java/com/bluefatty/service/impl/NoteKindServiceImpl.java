package com.bluefatty.service.impl;

import com.bluefatty.dao.TbNoteKindMapper;
import com.bluefatty.domain.TbNoteKind;
import com.bluefatty.service.INoteKindService;
import com.bluefatty.utils.CommonUtils;
import com.bluefatty.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
@Slf4j
@Service
public class NoteKindServiceImpl implements INoteKindService {
    @Autowired
    private TbNoteKindMapper tbNoteKindMapper;
}
