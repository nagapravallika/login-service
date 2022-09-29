package com.datafoundry.loginUserService.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.configuration.TenantAspectMongo;
import com.datafoundry.loginUserService.configuration.TenantAspectSql;
import com.datafoundry.loginUserService.model.request.RoleCollectionRequest;
import com.datafoundry.loginUserService.model.request.RoleEntityRequest;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;

@Service
public class RoleService {
    
    @Autowired
    DatabaseConnectionService dbConnection;
    
    @Autowired
    TenantAspectMongo mongoDB;
    
    @Autowired
    TenantAspectSql sqlDB;
    

    public ResponseEntity<UserResponse> findAll(Locale locale) {
    	       
        return dbConnection.getDatabaseInstance().findAllRoles(locale);
    }

    public ResponseEntity<UserResponse> findRoleByName(Locale locale, String roleName) {
    	
    	return dbConnection.getDatabaseInstance().findRoleByName(locale, roleName);
    }
    
    public ResponseEntity<RoleResponse> createRole(Locale locale, RoleEntityRequest roleEntityRequest) {
    	
    	return sqlDB.createRole(locale, roleEntityRequest, "Create Role Service");
    }
    
    public ResponseEntity<RoleResponse> createRole(Locale locale, RoleCollectionRequest roleColletionRequest) {
    	
    	return mongoDB.createRole(roleColletionRequest, "Create Role Service");
    }
    
    public ResponseEntity<RoleResponse> deleteRole(Locale locale, String roleName) {
    	
    	return dbConnection.getDatabaseInstance().deleteRole(locale, roleName, "Delete Role Mongo Service");
    }

    public ResponseEntity<RoleResponse> updateRole(Locale locale, String roleName, RoleEntityRequest roleRequest) {
    	
    	return sqlDB.updateRole(locale, roleName, roleRequest,"Update Role Service");
    }
    
    public ResponseEntity<RoleResponse> updateRole(Locale locale, String roleID, RoleCollectionRequest roleRequest) {
    	
    	return mongoDB.updateRole(roleID, roleRequest,"Update Role Service");
    }

}
