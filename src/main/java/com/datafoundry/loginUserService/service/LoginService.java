package com.datafoundry.loginUserService.service;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;

@Service
public class LoginService {

    @Autowired
    DatabaseConnectionService dbConnection;

    public ResponseEntity<LoginResponse> authenticateUser(Locale locale, @Valid  LoginRequest loginRequest) {

        return dbConnection.getDatabaseInstance().authenticateUser(locale, loginRequest);
    }
    

}
