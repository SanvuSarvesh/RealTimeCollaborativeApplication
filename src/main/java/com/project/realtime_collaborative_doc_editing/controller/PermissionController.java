package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/permission")
public class PermissionController {

    public BaseResponse allowUserToView(){
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    public BaseResponse allowUserToEdit(){
        BaseResponse baseResponse = new BaseResponse();
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
