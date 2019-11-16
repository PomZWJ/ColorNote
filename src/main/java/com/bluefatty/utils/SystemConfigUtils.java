package com.bluefatty.utils;

import com.bluefatty.constants.RedisKeyPrefix;
import com.bluefatty.dao.TbSystemConfigMapper;
import com.bluefatty.domain.TbSystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-12
 */
@Slf4j
@Component
public class SystemConfigUtils {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private TbSystemConfigMapper tbSystemConfigMapper;

    private Map<String, String> systemConfigMap = new HashMap<>();
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

    public void setMap(){
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
}
