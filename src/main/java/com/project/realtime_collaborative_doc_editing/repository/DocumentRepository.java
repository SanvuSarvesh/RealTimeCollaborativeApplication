package com.project.realtime_collaborative_doc_editing.repository;

import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DocumentRepository extends MongoRepository<DocumentDetails,String> {

    Optional<DocumentDetails> findByDocumentId(String documentId);

    Optional<DocumentDetails> findByDocumentTitle(String documentTitle);

}
