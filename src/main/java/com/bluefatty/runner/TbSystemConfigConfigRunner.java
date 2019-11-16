package com.bluefatty.runner;

import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.dao.TbSystemConfigMapper;
import com.bluefatty.domain.TbSystemConfig;
import com.bluefatty.utils.RedisUtils;
import com.bluefatty.utils.SystemConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-07
 */
@Slf4j
@Component
public class TbSystemConfigConfigRunner implements ApplicationRunner {

    @Autowired
    private SystemConfigUtils systemConfigUtils;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("============start--读取系统配置(TB_SYSTEM_CONFIG)============");
            systemConfigUtils.setMap();
            log.info("============success--读取系统配置(TB_SYSTEM_CONFIG)============");
        } catch (Exception e) {
            log.error("读取系统配置(TB_SYSTEM_CONFIG)失败,原因={}", e);
        }
    }



}
