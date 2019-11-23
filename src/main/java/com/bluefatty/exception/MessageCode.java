package com.bluefatty.exception;

/**
 * 错误代码
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public enum MessageCode {

    SUCCESS("000000","处理成功"),
    ERROR_USERID_IS_NULL("000001","用户ID不能为空"),
    ERROR_VERIFICATION_CODE_IS_NULL("000002","验证码不能为空"),
    ERROR_VERIFICATION_CODE_IS_NOT_EXIST("000003","验证码不存在,请重试"),
    ERROR_TOKEN_IS_NOT_EXIST("000004","Token不存在"),
    ERROR_TOKEN_IS_NOT_OK("000005","Token不正确"),
    ERROR_CHECK_SIGN("000006","Token不正确"),
    ERROR_TOKEN_IS_NULL("000007","Token为空"),
    ERROR_VERIFICATION_CODE_IS_NOT_OK("000008","验证码不正确,请重试"),
    ERROR_NOTE_CONTENT_IS_NULL("000009","笔记内容不能为空"),
    ERROR_NOTE_TIME_IS_NULL("000010","笔记时间不能为空"),
    ERROR_NOTE_KIND_IS_NULL("000011","笔记类型不能为空"),
    ERROR_NOTE_IS_NOT_EXIST("000012","笔记信息不存在"),
    ERROR_NOTE_IS_NOT_SINGLE("000013","笔记信息不唯一"),
    ERROR_NOTE_ID_IS_NULL("000014","笔记编号不能为空"),
    ERROR_FAV_STATE_IS_NULL("000015","收藏状态不能为空"),
    ERROR_NOTE_KIND_NAME_IS_NULL("000016","笔记类型名称不能为空"),
    ERROR_NOTE_KIND_URL_IS_NULL("000017","笔记类型URL不能为空"),
    ERROR_UNKOWN("999999","未知错误"),
    STARTUP_SUCCESS("000001","启动成功"),
    STARTUP_ERROR("000002","启动失败");
    String code;
    String msg;
    MessageCode(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "[code="+code+",message="+msg+"]";
    }
}
