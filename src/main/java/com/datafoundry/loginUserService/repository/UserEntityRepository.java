package com.datafoundry.loginUserService.repository;

import com.datafoundry.loginUserService.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}
