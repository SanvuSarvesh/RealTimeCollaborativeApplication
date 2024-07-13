package com.project.realtime_collaborative_doc_editing.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public <T> T getFromRedis(String key, Class<T> entityClass){

        try {
            Object object = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue((JsonParser) object, entityClass);
            //return objectMapper.readValue(object.toString(), entityClass);
        }catch (Exception exception){
            exception.getStackTrace();
            log.info("Redis Service : Something went wrong, in getting the data.");
        }
        return null;
    }

    public void setIntoRedis(String key, Object object, Long expiryTime){

        try {
             redisTemplate.opsForValue().set(key,object,expiryTime, TimeUnit.SECONDS);
        }catch (Exception exception){
            exception.getStackTrace();
            log.info("Redis Service : Something went wrong, in setting the data.");
        }

    }

}
