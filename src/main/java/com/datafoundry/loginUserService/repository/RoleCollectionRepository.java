package com.datafoundry.loginUserService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.datafoundry.loginUserService.model.RoleCollection;

public interface RoleCollectionRepository extends MongoRepository<RoleCollection, String> {

  RoleCollection findByRoleName(String roleName);
  RoleCollection findByRoleId(String id);
  boolean existsByRoleName(String roleName);

}
