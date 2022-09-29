package com.datafoundry.loginUserService.repository;

import com.datafoundry.loginUserService.model.GenerateToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  
public interface GenerateTokenRepository extends CrudRepository<GenerateToken, String> {

}
