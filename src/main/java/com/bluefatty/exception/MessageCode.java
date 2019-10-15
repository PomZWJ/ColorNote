package com.bluefatty.exception;

/**
 * 错误代码
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public enum MessageCode {

    SUCCESS("000000","处理成功"),
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
