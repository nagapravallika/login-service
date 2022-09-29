package com.datafoundry.loginUserService.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.configuration.TenantAspectMongo;
import com.datafoundry.loginUserService.configuration.TenantAspectSql;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserCollectionRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;

@Service
public class UserService {
    
    @Autowired
    DatabaseConnectionService dbConnection;
    
    @Autowired
    TenantAspectMongo mongoDB;
    
    @Autowired
    TenantAspectSql sqlDB;

    public ResponseEntity<UserResponse> findAll(Locale locale) {
        
        return dbConnection.getDatabaseInstance().findAllUsers(locale);
    }

    public ResponseEntity<UserResponse> findByEmailID(Locale locale, String email) {

        return dbConnection.getDatabaseInstance().findUserByEmailID(locale, email);
    }
    
    public ResponseEntity<UserResponse> changePassword(Locale locale, String emailID, String oldPassword, String newPassword, String serviceName) 
    {

        return dbConnection.getDatabaseInstance().changePassword(locale, emailID, oldPassword, newPassword, "Change Password Service");
    }
    
    public ResponseEntity<UserResponse> assignRolesToUser(Locale locale, String[] roleNames, String emailID) {

        return dbConnection.getDatabaseInstance().assignRolesToUser(locale, roleNames, emailID, "Assign Roles to User Service");
    }
    
    public ResponseEntity<UserResponse> revokeRolesFromUser(Locale locale, String[] roleNames, String emailID) {

    	return dbConnection.getDatabaseInstance().revokeRolesFromUser(locale, roleNames, emailID, "Revoke Roles from User Service");
    }

    public ResponseEntity<UserResponse> updateUserProfile(Locale locale, String emailID, String token, UserEntityRequest userRequest) {

        return sqlDB.updateUserProfile(locale, emailID, userRequest, token, "Update User Profile Service");
    }
    
    public ResponseEntity<UserResponse> updateUserProfile(Locale locale, String emailID, String tenantID, UserCollectionRequest userRequest) {

        return mongoDB.updateUserProfile(locale, emailID, tenantID, userRequest, "Update User Profile Service");
    }
    
    
    public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(Locale locale, SecurityQuestionsRequest securityQuestionsRequest) {

        return dbConnection.getDatabaseInstance().validateSecurityQuestions(locale, securityQuestionsRequest, "Update User Profile Service");
    }
    
    public ResponseEntity<UserResponse> logout(Locale locale, String userID) {

        return dbConnection.getDatabaseInstance().logout(locale, userID);
    }
    
    public ResponseEntity<UserResponse> toggleStatus(Locale locale, String userID, String status) {

        return dbConnection.getDatabaseInstance().toggleStatus(locale, userID, status);
    }
    
    public ResponseEntity<UserResponse> registerUser(Locale locale, UserEntityRequest userRequest)
    {
        boolean userExists = sqlDB.existsByEmail(locale, userRequest.getEmail());

        if (userExists) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserResponse("Error: Email is already in use!", false, 400));
        }
        return sqlDB.saveUserProfile(locale, userRequest, "Register User Service");
    }
}
