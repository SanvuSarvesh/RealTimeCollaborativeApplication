package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.service.impl.DocumentServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    @Autowired
    private  DocumentServiceImpl documentService;

    private final Logger Logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/create")
    public BaseResponse createNewDocument(@RequestBody DocumentReqDto documentReqDto){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : createNewDocument");
        BaseResponse baseResponse = documentService.createNewDocument(documentReqDto);
        Logger.info("Time Taken by createNewDocument : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

    @GetMapping("/get-by-id/{id}")
    public BaseResponse getDocumentById(@PathVariable("id") String documentID){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : getDocumentById");
        BaseResponse baseResponse = documentService.getDocumentById(documentID);
        Logger.info("Time Taken by getDocumentById : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

    @GetMapping("/get-by-title/{title}")
    public BaseResponse getDocumentByDocumentTitle(@PathVariable("title") String documentTitle){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : getDocumentByDocumentTitle");
        BaseResponse baseResponse = documentService.getDocumentByDocumentTitle(documentTitle);
        Logger.info("Time Taken by getDocumentByDocumentTitle : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

    @PutMapping("/edit/{id}")
    public BaseResponse editDocument(@PathVariable("id") String documentId, @RequestBody DocumentReqDto documentReqDto){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : editDocument");
        BaseResponse baseResponse = documentService.editDocument(documentId,documentReqDto);
        Logger.info("Time Taken by editDocument : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse deleteDocument(@PathVariable("id") String documentId){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : deleteDocument");
        BaseResponse baseResponse = documentService.deleteDocument(documentId);
        Logger.info("Time Taken by deleteDocument : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

    @GetMapping("search/{keyword}")
    public BaseResponse searchDocumentsByKeyword(@PathVariable String keyword){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the DocumentController : searchDocumentsByKeyword");
        BaseResponse baseResponse = new BaseResponse();
        Logger.info("Time Taken by searchDocumentsByKeyword : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return baseResponse;
    }

}
