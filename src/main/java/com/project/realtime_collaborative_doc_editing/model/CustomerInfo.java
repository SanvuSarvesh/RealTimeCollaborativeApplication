package com.project.realtime_collaborative_doc_editing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customerInfo")
public class CustomerInfo {

    @Id
    private String customerId;

    private String customerName;

    private String mobileNo;

    private String emailId;

    private String state;

    private String city;

    private double netWorth;

}
