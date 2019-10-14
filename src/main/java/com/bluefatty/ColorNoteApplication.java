package com.bluefatty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动类
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-14
 */
@RestController
@SpringBootApplication
public class ColorNoteApplication {
    /**
     * 启动方法
     * @param args
     */
    public static void main(String[] args) {
        try {
            SpringApplication.run(ColorNoteApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 测试接口
     * @return
     */
    @RequestMapping("/hello")
    public String hello(){
        return "Successful startup";
    }
}
