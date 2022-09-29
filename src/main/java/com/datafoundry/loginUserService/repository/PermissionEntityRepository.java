package com.datafoundry.loginUserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.PermissionEntity;

@Repository
public interface PermissionEntityRepository extends JpaRepository<PermissionEntity, String> {

    PermissionEntity findByModuleName(String moduleName);
    boolean existsByPermissionID(String permissionID); 
    PermissionEntity findByPermissionID(String permissionID);
}
