package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.service.impl.PermissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/permission")
public class PermissionController {

    private final PermissionServiceImpl permissionService;

    private final org.slf4j.Logger Logger = LoggerFactory.getLogger(PermissionController.class);

    @PutMapping("allow-to-view/{id}/{username}")
    public ResponseEntity<BaseResponse> allowUserToView(@PathVariable("id") String documentId, @PathVariable("username") String username){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the PermissionController : allowUserToView");
        BaseResponse baseResponse = permissionService.allowUserToView(documentId,username);
        Logger.info("Time Taken by allowUserToView : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("allow-to-edit/{id}/{username}")
    public ResponseEntity<BaseResponse> allowUserToEdit(@PathVariable("id") String documentId, @PathVariable("username") String username,
                                        @RequestBody DocumentReqDto documentReqDto){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the PermissionController : allowUserToEdit");
        BaseResponse baseResponse = permissionService.allowUserToEdit(documentId,username,documentReqDto);
        Logger.info("Time Taken by allowUserToEdit : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("/withdraw-view-access/{documentId}")
    public ResponseEntity<BaseResponse> withDrawViewPermission(String documentId){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the PermissionController : withDrawViewPermission");
        BaseResponse baseResponse = permissionService.withDrawViewPermission(documentId);
        Logger.info("Time Taken by withDrawViewPermission : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("/withdraw-edit-access/{documentId}")
    public ResponseEntity<BaseResponse> withdrawEditPermission(String documentId){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the PermissionController : withdrawEditPermission");
        BaseResponse baseResponse = permissionService.withdrawEditPermission(documentId);
        Logger.info("Time Taken by withdrawEditPermission : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("access-request/{documentId}")
    public ResponseEntity<BaseResponse> requestForAccess(@PathVariable("documentId") String documentId){
        long startTime = System.currentTimeMillis();
        Logger.info("Inside the PermissionController : requestForAccess");
        BaseResponse baseResponse = permissionService.requestForAccess(documentId);
        Logger.info("Time Taken by requestForAccess : {} : "+(System.currentTimeMillis() - startTime),baseResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
