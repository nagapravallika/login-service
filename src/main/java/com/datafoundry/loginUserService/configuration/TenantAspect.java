package com.datafoundry.loginUserService.configuration;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;

public interface TenantAspect {
    
    ResponseEntity<RoleResponse> deleteRole(Locale locale, String roleID, String serviceName);

    ResponseEntity<LoginResponse> authenticateUser(Locale locale, LoginRequest loginRequest);

    ResponseEntity<UserResponse> findAllUsers(Locale locale);

    ResponseEntity<UserResponse> findUserByEmailID(Locale locale, String email);
    
    ResponseEntity<UserResponse> findAllRoles(Locale locale);

    ResponseEntity<UserResponse> findRoleByName(Locale locale, String roleName);
    
    ResponseEntity<UserResponse> toggleStatus(Locale locale, String roleName, String status);
    
    ResponseEntity<UserResponse> changePassword(Locale locale, String emailID, String oldPassword, String newPassword, String serviceName);
    
    public ResponseEntity<UserResponse> assignRolesToUser(Locale locale, String[] roleIds, String emailID,  String serviceName);
    
    ResponseEntity<UserResponse> revokeRolesFromUser(Locale locale, String[] roleIds, String emailID, String serviceName);
        
    String getDatabaseType();
    
    boolean existsByEmail(Locale locale, String emailID);
    
    ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(Locale locale, SecurityQuestionsRequest securityQuestionsRequest, String serviceName);
    
    ResponseEntity<UserResponse> logout(Locale locale, String userID);

}
