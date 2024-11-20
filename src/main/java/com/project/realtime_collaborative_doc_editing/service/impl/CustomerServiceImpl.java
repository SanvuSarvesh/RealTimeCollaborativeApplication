package com.project.realtime_collaborative_doc_editing.service.impl;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.CustomerInfoDto;
import com.project.realtime_collaborative_doc_editing.model.CustomerInfo;
import com.project.realtime_collaborative_doc_editing.repository.CustomerRepository;
import com.project.realtime_collaborative_doc_editing.service.CustomerService;
import com.project.realtime_collaborative_doc_editing.utils.ExcelUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public BaseResponse saveCustomerInfo(MultipartFile file){
        BaseResponse baseResponse = new BaseResponse();
        try {
            boolean isValidFile = ExcelUtils.checkFileType(file);
            if(!isValidFile){
                throw new RuntimeException("Invalid file type, Please try with Excel file.");
            }
            List<CustomerInfo> customerRecords = ExcelUtils.getRecordsFromExcelFile(file.getInputStream());
            customerRepository.saveAll(customerRecords);

            // We can also save the data individually
      /**
             for(CustomerInfo customerInfo : customerRecords){
                 customerRepository.save(customerInfo);
             }
       */

        }catch (Exception exception){
            baseResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
            baseResponse.setMessage("Failed to process the records.");
            baseResponse.setSuccess(false);
            return baseResponse;
        }
        baseResponse.setStatusCode(HttpStatus.CREATED.toString());
        baseResponse.setMessage("Records uploaded successfully.");
        baseResponse.setSuccess(true);
        return baseResponse;
    }

    public BaseResponse getCustomerInfo(String customerId){
        return null;
    }

    public BaseResponse updateCustomerInfo(String customerId, CustomerInfoDto customerInfoDto){
        return null;
    }

    public BaseResponse deleteCustomerInfo(String customerId){
        return null;
    }

}
