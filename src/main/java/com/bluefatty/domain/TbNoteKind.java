package com.bluefatty.domain;

import lombok.Data;

/**
 * 笔记分类
 *
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-05
 */
@Data
public class TbNoteKind {
    private String noteKindId;
    private String userId;
    private String noteKindName;
    private String createDate;
    private String createTime;
    private String kindIconUrl;

}