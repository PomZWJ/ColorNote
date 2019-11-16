package com.bluefatty.utils;

import java.util.Random;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
public class PhoneCodeUtils {
    /**
     * 获取6位随机生成的验证码
     * @return
     */
    public static String getVerifyCode() {
        String[] verifyString = new String[] { "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9" };
        Random random = new Random(System.currentTimeMillis());
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int rd = random.nextInt(10);
            verifyBuilder.append(verifyString[rd]);
        }
        String verifyCode = verifyBuilder.toString();
        return verifyCode;
    }
}
