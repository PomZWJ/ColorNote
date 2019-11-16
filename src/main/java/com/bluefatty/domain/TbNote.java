package com.bluefatty.domain;

import lombok.Data;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
@Data
public class TbNote {

    private String noteId;

    private String noteKindId;

    private String userId;

    private String noteContent;

    private String isFav;

    private String isDelete;

    private String deleteDate;

    private String noteTime;

    private String createTime;

    private String createDate;
}