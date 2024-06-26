package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;


public interface PermissionService {

    BaseResponse allowUserToView();

    BaseResponse allowUserToEdit();

    BaseResponse withDrawViewPermission();

    BaseResponse withEditViewPermission();

    BaseResponse requestForViewAccess();

    BaseResponse requestForEditAccess();

}
