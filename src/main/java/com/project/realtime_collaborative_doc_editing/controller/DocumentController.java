package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.service.impl.DocumentServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    @Autowired
    private final DocumentServiceImpl documentService;

    @PostMapping("/create")
    public BaseResponse createNewDocument(@RequestBody DocumentReqDto documentReqDto){
        BaseResponse baseResponse = documentService.createNewDocument(documentReqDto);
        return baseResponse;
    }

    @GetMapping("/get-by-id/{id}")
    public BaseResponse getDocumentById(@PathVariable("id") String documentID){
        BaseResponse baseResponse = documentService.getDocumentById(documentID);
        return baseResponse;
    }

    @GetMapping("/get-by-title/{title}")
    public BaseResponse getDocumentByDocumentTitle(@PathVariable("title") String documentTitle){
        BaseResponse baseResponse = documentService.getDocumentByDocumentTitle(documentTitle);
        return baseResponse;
    }

    @PutMapping("/edit")
    public BaseResponse editDocument(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    @PutMapping("/delete")
    public BaseResponse deleteDocument(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    @GetMapping("search/{keyword}")
    public BaseResponse searchDocumentsByKeyword(@PathVariable String keyword){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

}
