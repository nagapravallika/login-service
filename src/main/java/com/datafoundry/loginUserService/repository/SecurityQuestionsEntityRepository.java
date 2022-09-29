package com.datafoundry.loginUserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.SecurityQuestionsEntity;

@Repository
public interface SecurityQuestionsEntityRepository extends JpaRepository<SecurityQuestionsEntity, String> {

  Boolean existsByEmail(String email);

  SecurityQuestionsEntity findByEmail(String email);
  
  SecurityQuestionsEntity findByEmailAndTenantID(String email, String tenantID);

}
