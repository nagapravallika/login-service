package com.datafoundry.loginUserService.configuration;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.LoginUserServiceApplication;
import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.request.RoleEntityRequest;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.util.UtilSql;

//@Component
@Service("SQLDatabaseService")
public class TenantAspectSql implements TenantAspect{

    @Autowired
    UtilSql utilSql;
    
    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);

    @Override
    public boolean existsByEmail(Locale locale, String emailID)
    {
    	return utilSql.existsByEmailID(locale, emailID);
    }
    
    public ResponseEntity<UserResponse> saveUserProfile(Locale locale, UserEntityRequest userRequest, String serviceName) {
        return utilSql.saveUserProfile(locale, userRequest, serviceName);

    }    
  
    public ResponseEntity<UserResponse> updateUserProfile(Locale locale, String emailID, UserEntityRequest userRequest, String token, String serviceName) {
        return utilSql.updateUserProfile(locale, emailID, userRequest, token, serviceName);

    }
    
    public ResponseEntity<RoleResponse> createRole(Locale locale, RoleEntityRequest roleEntityRequest, String serviceName) 
    {
    	if(roleEntityRequest instanceof RoleEntityRequest)
    		return utilSql.createRole(locale, (RoleEntityRequest)roleEntityRequest, serviceName);
    	else
    		return ResponseEntity.ok(new RoleResponse( "Invalid data. Cannot create the role", false, 400, null ));  
    }
    
    @Override
    public ResponseEntity<RoleResponse> deleteRole(Locale locale, String roleName, String serviceName) 
    {
    	return utilSql.deleteRole(locale, roleName, serviceName);
    }
    
    public ResponseEntity<RoleResponse> updateRole(Locale locale, String roleName, RoleEntityRequest roleRequest, String serviceName) 
    {
    	return utilSql.updateRole(locale, roleName, roleRequest, serviceName);
    }

    @Override
    public ResponseEntity<LoginResponse> authenticateUser(Locale locale, LoginRequest loginRequest) {
    	System.out.println("inside sql aspect, calling auth on util000");
        return utilSql.authenticateUser(locale, loginRequest);
    }

    @Override
    public ResponseEntity<UserResponse> findAllUsers(Locale locale) {
        return utilSql.findAllUsers(locale);
    }

    @Override
    public ResponseEntity<UserResponse> findUserByEmailID(Locale locale, String email) {
        return utilSql.findUserByEmailID(locale, email);
    }
    
    @Override
    public ResponseEntity<UserResponse> findAllRoles(Locale locale) {
        return utilSql.findAllRoles(locale);
    }
    
    @Override
    public ResponseEntity<UserResponse> findRoleByName(Locale locale, String roleName) {
        return utilSql.findRoleByName(locale, roleName);
    }
    
    @Override
    public ResponseEntity<UserResponse> toggleStatus(Locale locale, String emailID, String status) {
        return utilSql.toggleStatus(locale, emailID, status);
    }
    
    @Override
    public ResponseEntity<UserResponse> changePassword(Locale locale, String emailID, String oldPassword, String newPassword, String serviceName) {
    	logger.info("Inside tenantsql's change password");
        return utilSql.changePassword(locale, emailID, oldPassword, newPassword, serviceName);
    }
    
    @Override
    public ResponseEntity<UserResponse> assignRolesToUser(Locale locale, String[] roles, String emailID,  String serviceName)
    {
    	return utilSql.assignRolesToUser(locale, roles, emailID, serviceName);
    }
    
    @Override
    public ResponseEntity<UserResponse> revokeRolesFromUser(Locale locale, String[] roleNames, String emailID, String serviceName)
    {
    	return utilSql.revokeRolesFromUser(locale, roleNames, emailID,  serviceName);
    }
    
    @Override
    public String getDatabaseType()
    {
    	return "SQL";
    }
    
    public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(Locale locale, SecurityQuestionsRequest securityQuestionsRequest, String serviceName)
    {
    	return utilSql.validateSecurityQuestions(locale, securityQuestionsRequest, serviceName);
    }
    
    public ResponseEntity<UserResponse> logout(Locale locale, String userID)
    {
    	return utilSql.logout(locale, userID);
    }

}
