package com.project.realtime_collaborative_doc_editing.dto;

import lombok.Data;

@Data
public class UpdatePasswordReqDto {

    private String emailID;

    private String newPassword;

    private String confirmPassword;
}
