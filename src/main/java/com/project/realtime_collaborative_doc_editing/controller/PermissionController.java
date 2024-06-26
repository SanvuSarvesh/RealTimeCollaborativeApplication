package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.DocumentReqDto;
import com.project.realtime_collaborative_doc_editing.service.impl.PermissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/permission")
public class PermissionController {

    private final PermissionServiceImpl permissionService;

    @PutMapping("allow-to-view/{id}/{username}")
    public BaseResponse allowUserToView(@PathVariable("id") String documentId, @PathVariable("username") String username){
        BaseResponse baseResponse = permissionService.allowUserToView(documentId,username);
        return baseResponse;
    }

    @PutMapping("allow-to-edit/{id}/{username}")
    public BaseResponse allowUserToEdit(@PathVariable("id") String documentId, @PathVariable("username") String username,
                                        @RequestBody DocumentReqDto documentReqDto){
        BaseResponse baseResponse = permissionService.allowUserToEdit(documentId,username,documentReqDto);
        return baseResponse;
    }

    public BaseResponse withDrawViewPermission(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    public BaseResponse withEditViewPermission(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    public BaseResponse requestForViewAccess(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    public BaseResponse requestForEditAccess(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

}
