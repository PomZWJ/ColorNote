package com.bluefatty.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
public class DateUtils {
    /**
     * 获取当前日期,需要定义格式
     * @return 当前时间字符串 yyyyMMdd
     */
    public static String getCurrentDate(String format) {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat(format);
        String dateChar = sim.format(date);
        return dateChar;
    }

    /**
     * 获取当前日期,不需要定义格式，默认是 yyyyMMdd
     * @return 当前时间字符串 yyyyMMdd
     */
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        String dateChar = sim.format(date);
        return dateChar;
    }
    /**
     * 获取当前时间
     *
     * @return 当前时间字符串 HHmmss
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("HHmmss");
        String dateChar = sim.format(date);
        return dateChar;
    }

    /**
     * 获取当前日期和时间
     * @return 当前时间字符串 yyyyMMddHHmmss
     */
    public static String getCurrentDateAndTime() {
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateChar = sim.format(date);
        return dateChar;
    }
}
