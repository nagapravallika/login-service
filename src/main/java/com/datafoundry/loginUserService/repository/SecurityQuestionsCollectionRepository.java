package com.datafoundry.loginUserService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.SecurityQuestionsCollection;

@Repository
public interface SecurityQuestionsCollectionRepository extends MongoRepository<SecurityQuestionsCollection, String> {

  Boolean existsByEmailId(String email);

  SecurityQuestionsCollection findByEmailId(String email);

}
