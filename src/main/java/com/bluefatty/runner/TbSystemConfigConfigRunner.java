package com.bluefatty.runner;

import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.dao.TbSystemConfigMapper;
import com.bluefatty.domain.TbSystemConfig;
import com.bluefatty.utils.RedisUtils;
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

    private Map<String, String> systemConfigMap = new HashMap<>();


    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private TbSystemConfigMapper tbSystemConfigMapper;

    private void setMap()throws Exception{
        systemConfigMap.clear();
        List<TbSystemConfig> tbSystemConfigs = tbSystemConfigMapper.selectAll();
        if (tbSystemConfigs != null && tbSystemConfigs.size() != 0) {
            for(TbSystemConfig tsc : tbSystemConfigs){
                systemConfigMap.put(tsc.getConfigKey(),tsc.getConfigValue());
            }
        }
        //3分钟过期
        redisUtils.cacheMap(RedisKeyPrefix.CACHE_SYSTEM_CONFIG,systemConfigMap,3, TimeUnit.MINUTES);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("============start--读取系统配置(TB_SYSTEM_CONFIG)============");
            this.setMap();
            log.info("============success--读取系统配置(TB_SYSTEM_CONFIG)============");
        } catch (Exception e) {
            log.error("读取系统配置(TB_SYSTEM_CONFIG)失败,原因={}", e);
        }
    }

    /**
     * 得到ConfigMap(一般不用)
     * @return
     */
    public Map<String,String> getConfigMap(){
        try{
            Map cacheMap = redisUtils.getCacheMap(RedisKeyPrefix.CACHE_SYSTEM_CONFIG);
            if(cacheMap != null){
                systemConfigMap.clear();
                systemConfigMap.putAll(cacheMap);
            }else{
                this.setMap();
            }
        }catch (Exception e){
            log.error("读取系统配置(TB_SYSTEM_CONFIG)失败(方法getConfigMap),原因={}", e);
        }
        return systemConfigMap;
    }

    /**
     * 根据configKey得到configValue
     * @param configKey
     * @return
     */
    public String getConfigValue(String configKey){
        try{
            Map cacheMap = redisUtils.getCacheMap(RedisKeyPrefix.CACHE_SYSTEM_CONFIG);
            if(cacheMap != null){
                systemConfigMap.clear();
                systemConfigMap.putAll(cacheMap);
            }else{
                this.setMap();
            }
        }catch (Exception e){
            log.error("读取系统配置(TB_SYSTEM_CONFIG)失败(方法getConfigValue),原因={}", e);
        }
        return systemConfigMap.get(configKey);
    }

}
