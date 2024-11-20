package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.CustomerInfoDto;

public interface CustomerService {

    BaseResponse saveCustomerInfo(CustomerInfoDto customerInfoDto);

    BaseResponse getCustomerInfo(String customerId);

    BaseResponse updateCustomerInfo(String customerId, CustomerInfoDto customerInfoDto);

    BaseResponse deleteCustomerInfo(String customerId);
}
