package com.project.realtime_collaborative_doc_editing.repository;

import com.project.realtime_collaborative_doc_editing.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserProfile, String>
{
  Optional<UserProfile> findByUsername(String username);

  Optional<UserProfile> findByEmail(String email);

}
