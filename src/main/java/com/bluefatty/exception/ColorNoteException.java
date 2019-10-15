package com.bluefatty.exception;

/**
 * ColorNote异常类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public class ColorNoteException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public ColorNoteException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ColorNoteException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ColorNoteException(Throwable e) {
        super(e);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
