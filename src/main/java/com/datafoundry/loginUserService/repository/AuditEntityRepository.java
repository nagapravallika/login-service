package com.datafoundry.loginUserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.AuditEntity;

@Repository
public interface AuditEntityRepository extends JpaRepository<AuditEntity, Long> {

    AuditEntity[] findByModifiedBy(String modifiedBy);
    AuditEntity[] findByModifiedUserRole(String modifiedUserRole);
    AuditEntity[] findByOperation(String operation);
    AuditEntity[] findByServiceName(String serviceName);
    AuditEntity[] findByEmailID(String emailID);
}
