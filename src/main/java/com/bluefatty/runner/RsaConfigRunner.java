package com.bluefatty.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * rsa启动读取文件类
 *
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Slf4j
@Component
public class RsaConfigRunner implements ApplicationRunner {
    public static String PUBLIC_KEY;
    public static String PRIVATE_KEY;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("============start--读取RSA的公钥和私钥============");
            File privateKeyFile = ResourceUtils.getFile("classpath:rsa/RSA-PRIVATE-KEY.txt");
            File publicKeyFile = ResourceUtils.getFile("classpath:rsa/RSA-PUBLIC-KEY.txt");
            PRIVATE_KEY = this.readTxtFile(privateKeyFile);
            PUBLIC_KEY = this.readTxtFile(publicKeyFile);
            log.info("============success--读取RSA的公钥和私钥============");
        }catch (FileNotFoundException e){
            log.error("RSA的公钥和私钥的文件伟找到,原因={}",e);
        }catch (Exception e){
            log.error("读取RSA的公钥和私钥文件失败,原因={}",e);
        }
    }

    /**
     * 读取流中的文件数据
     *
     * @return
     */
    private String readTxtFile(File file) throws Exception {
        StringBuilder result = new StringBuilder();
        // 创建字符读取流
        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(in);
            String s = null;
            boolean firstLine = true;
            while ((s = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    result.append("\n");
                }
                result.append(s);
            }
            return result.toString();

        } finally {
            if (null != br) {
                // 关闭输入流
                br.close();
            }
            if (null != in) {
                in.close();
            }

        }
    }
}
