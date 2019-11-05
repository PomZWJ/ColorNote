package com.bluefatty.facade;

import com.bluefatty.service.TbNoteKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Component
public class NoteKindFacade {
    @Autowired
    private TbNoteKindService tbNoteKindService;


    public void userInitInsertKind(){

    }
}
