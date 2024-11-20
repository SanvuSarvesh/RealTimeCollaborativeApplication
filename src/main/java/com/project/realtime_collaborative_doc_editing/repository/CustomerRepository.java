package com.project.realtime_collaborative_doc_editing.repository;

import com.project.realtime_collaborative_doc_editing.model.CustomerInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerInfo,String> {
}
