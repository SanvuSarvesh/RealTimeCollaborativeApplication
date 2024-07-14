package com.project.realtime_collaborative_doc_editing.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("spring.redis.host")
    private String redisServer;

    @Value("spring.redis.port")
    private String redisPort;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getFromRedis(String key, Class<T> entityClass){

        try {
            Object object = redisTemplate.opsForValue().get(key);
            System.out.println("Inside the try block in Redis service Object before converting to Doc : "+object);
            //ObjectMapper objectMapper = new ObjectMapper();
            //DocumentDetails details = (DocumentDetails) objectMapper.readValue( object.toString(), entityClass);
            //System.out.println("-------> type caste details : "+details);
            return new ObjectMapper().readValue(object.toString(),entityClass);
            //return objectMapper.readValue( object.toString(), entityClass);
            //return objectMapper.readValue(object.toString(), entityClass);
        }catch (Exception exception){
            exception.getStackTrace();
            log.info("Redis Service : Something went wrong, in fetching the data from Redis server.");
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
