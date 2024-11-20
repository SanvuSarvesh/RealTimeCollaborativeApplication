package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.CustomerInfoDto;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    BaseResponse saveCustomerInfo(MultipartFile file);

    BaseResponse getCustomerInfo(String customerId);

    BaseResponse updateCustomerInfo(String customerId, CustomerInfoDto customerInfoDto);

    BaseResponse deleteCustomerInfo(String customerId);
}
