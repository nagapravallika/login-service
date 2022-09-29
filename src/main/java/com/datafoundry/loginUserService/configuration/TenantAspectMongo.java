package com.datafoundry.loginUserService.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.request.RoleCollectionRequest;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserCollectionRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.util.UtilMongo;

//@Component
@Service("MongoDatabaseService")
public class TenantAspectMongo implements TenantAspect{

    @Autowired
    UtilMongo utilMongo;
    
    @Override
    
    public boolean existsByEmail(Locale locale, String emailID)
    {
    	return utilMongo.existsByEmailId(emailID);
    }
    
    //Dummy implementation
    @Override
    public ResponseEntity<UserResponse> toggleStatus(Locale locale, String emailID, String status) {
        return null;
    }

    public ResponseEntity<UserResponse> saveUserProfile(Locale locale, UserCollectionRequest userRequest, String serviceName) {
       return utilMongo.saveUserProfile(userRequest, serviceName);

    }
    
    public ResponseEntity<UserResponse> updateUserProfile(Locale locale, String emailID, String tenantID, UserCollectionRequest userRequest, String serviceName) {
        return utilMongo.updateUserProfile(emailID, tenantID, userRequest, serviceName);

    }
    
    
    public ResponseEntity<UserResponse> validateSecurityQuestions(Locale locale, UserEntityRequest userRequest, String serviceName)
    {
    	return null;
    }

    
    public ResponseEntity<RoleResponse> createRole(RoleCollectionRequest roleCollectionRequest, String serviceName) 
    {
    	
    	if(roleCollectionRequest instanceof RoleCollectionRequest)
    	{
    		return utilMongo.createRole((RoleCollectionRequest)roleCollectionRequest, serviceName);
    	}
        
    	return ResponseEntity.ok(new RoleResponse( "Invalid data. Cannot create the role", false, 400, null )); 
    }
    
    @Override
    public ResponseEntity<RoleResponse> deleteRole(Locale locale, String roleID, String serviceName) {
        return utilMongo.deleteRole(roleID, serviceName);
    }
    
    public ResponseEntity<RoleResponse> updateRole(String roleID, RoleCollectionRequest roleRequest, String serviceName) {
        return utilMongo.updateRole(roleID, roleRequest, serviceName);
    }

    @Override
    public ResponseEntity<LoginResponse> authenticateUser(Locale locale, LoginRequest loginRequest) {
        return utilMongo.authenticateUser(loginRequest);
    }

    //dummy implementation
    @Override
    public ResponseEntity<UserResponse> findAllUsers(Locale locale) {
        return null;
    	//return utilMongo.findAllUsers();
    }

    //dummy implementation
    @Override
    public ResponseEntity<UserResponse> findUserByEmailID(Locale locale, String email) {
        return null;
    	//return utilMongo.findUserByEmailID(email);
    }
    
    @Override
    public ResponseEntity<UserResponse> changePassword(Locale locale, String emailID, String oldPassword, String newPassword, String serviceName) {
        return utilMongo.changePassword(emailID, oldPassword, newPassword, serviceName);
    }
    
    //dummy implementation
    @Override
    public ResponseEntity<UserResponse> findAllRoles(Locale locale) {
        return null;
    	//return utilMongo.findAllRoles();
    }
    
    @Override
    public ResponseEntity<UserResponse> findRoleByName(Locale locale, String roleName) {
        return utilMongo.findRoleByName(roleName);
    }
    
    //dummy implementation
    @Override
    public ResponseEntity<UserResponse> assignRolesToUser(Locale locale, String[] roleIds, String emailID, String serviceName) {
        return null;
    	//return utilMongo.assignRolesToUser(roleId, emailID, tenantID, serviceName);
    }
    
    //dummy implementation
    @Override
    public ResponseEntity<UserResponse> revokeRolesFromUser(Locale locale, String[] roleIDs, String emailID, String serviceName) {
        return null;
    	//return utilMongo.revokeRolesFromUser(role, emailID, clientID, serviceName);
    }

    @Override
    public String getDatabaseType() {
        return "Mongo";
    }

    public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(Locale locale, SecurityQuestionsRequest securityQuestionsRequest, String serviceName)
    {
    	return utilMongo.validateSecurityQuestions(securityQuestionsRequest, serviceName);
    }
    
    //dummy implementation
    public ResponseEntity<UserResponse> logout(Locale locale, String userID)
    {
    	return null;
    }
}
