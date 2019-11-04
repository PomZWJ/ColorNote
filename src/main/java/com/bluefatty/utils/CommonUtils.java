package com.bluefatty.utils;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-04
 */
public class CommonUtils {
    /**
     * 得到异常抛出的类和方法名
     * @return
     */
    public static String getCurrentClassAndMethod(){
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        StringBuffer exceptionPrefix = new StringBuffer("");
        exceptionPrefix.append(className).append("(").append(currentMethodName).append(")");
        return exceptionPrefix.toString();
    }
}
