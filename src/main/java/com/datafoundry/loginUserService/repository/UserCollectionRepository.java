package com.datafoundry.loginUserService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.UserCollection;

@Repository
public interface UserCollectionRepository extends MongoRepository<UserCollection, String> {

  Boolean existsByEmailId(String email);

  UserCollection findByEmailId(String email);

}
