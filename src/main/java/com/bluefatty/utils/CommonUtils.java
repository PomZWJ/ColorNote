package com.bluefatty.utils;

import java.util.UUID;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-04
 */
public class CommonUtils {
    /**
     * 返回UUID
     * @return
     */
    public static String getUuid(){
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        return s.replaceAll("-","");
    }
}
