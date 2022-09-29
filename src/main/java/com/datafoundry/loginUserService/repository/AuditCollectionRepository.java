package com.datafoundry.loginUserService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.datafoundry.loginUserService.model.AuditCollection;

@Repository
public interface AuditCollectionRepository extends MongoRepository<AuditCollection, String> {

	AuditCollection[] findByModifiedBy(String modifiedBy);
	AuditCollection[] findByModifiedUserRole(String modifiedUserRole);
	AuditCollection[] findByOperation(String operation);
	AuditCollection[] findByServiceName(String serviceName);
	AuditCollection[] findByEmailID(String emailID);
}
