package com.bluefatty.utils;

/**
 * String工具类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-14
 */
public class StringUtils {
    /**
     * 判断字符串是不是为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 获取字符串
     * @param o
     * @return
     */

    public static String getValue(Object o){
        if(o == null){
            return "";
        }
        return o.toString();
    }
}
