package com.bluefatty;

import com.bluefatty.exception.MessageCode;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
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
@Slf4j
@MapperScan(value = "com.bluefatty.dao")
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
            log.info("启动..{}", MessageCode.STARTUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            log.error("启动..{},原因={}",MessageCode.STARTUP_ERROR,e);
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
