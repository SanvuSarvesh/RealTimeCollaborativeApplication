package com.project.realtime_collaborative_doc_editing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/test/hashmap")
public class TestApiHashMap {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping
    public String saveNewRecord(@RequestBody HashMap<String,Object> reqObject){
        String collectionName = "hashmap_collection_db";
        Object obj = mongoTemplate.insert(reqObject, collectionName);
        System.out.println("obj : "+obj);
        return "new Object saved ";
    }


}
