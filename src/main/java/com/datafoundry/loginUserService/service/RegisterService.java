package com.datafoundry.loginUserService.service;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.configuration.TenantAspectMongo;
import com.datafoundry.loginUserService.configuration.TenantAspectSql;
import com.datafoundry.loginUserService.model.request.UserCollectionRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.UserResponse;

@Service
public class RegisterService {

    @Autowired
    TenantAspectMongo mongoDB;
    
    @Autowired
    TenantAspectSql sqlDB;


    public ResponseEntity<UserResponse> registerUser(Locale locale, @Valid UserCollectionRequest userRequest) {

        boolean userExists = mongoDB.existsByEmail(locale, userRequest.getEmail());

        if (userExists) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserResponse("Error: Email is already in use!", false, 400));
        }
        return mongoDB.saveUserProfile(locale, userRequest, "Register User Service");
    }
    
    
    public ResponseEntity<UserResponse> registerUser(Locale locale, @Valid UserEntityRequest userRequest) {

        boolean userExists = sqlDB.existsByEmail(locale, userRequest.getEmail());

        if (userExists) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserResponse("Error: Email is already in use!", false, 400));
        }
        return sqlDB.saveUserProfile(locale, userRequest, "Register User Service");
    }
    
}
