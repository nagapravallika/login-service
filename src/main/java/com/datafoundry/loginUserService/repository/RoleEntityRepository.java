package com.datafoundry.loginUserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.RoleEntity;

@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, String> {

    RoleEntity findByRoleName(String roleName);
    RoleEntity findByRoleID(String roleID);
    Boolean existsByRoleID(String roleID);
    Boolean existsByRoleName(String roleName);
}
