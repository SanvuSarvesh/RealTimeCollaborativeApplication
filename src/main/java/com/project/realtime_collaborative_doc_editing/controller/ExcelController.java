package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.service.CustomerService;
import com.project.realtime_collaborative_doc_editing.service.impl.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel/api")
public class ExcelController {

    private final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> saveCustomerRecords(@RequestParam("file")MultipartFile file){
        BaseResponse baseResponse = customerService.saveCustomerInfo(file);
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }

}
