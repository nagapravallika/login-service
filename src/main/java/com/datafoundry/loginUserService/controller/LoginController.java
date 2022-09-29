package com.datafoundry.loginUserService.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.service.LoginService;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "LoginController", tags = { "Login User config APIs" })
@RequestMapping("authservice/v1")
public class LoginController {

	@Autowired
	LoginService loginService;
	
	  @ApiOperation(value ="Login User")
	  @ApiResponses(value = { @ApiResponse(code = 200, message = "You are sussessfully login in to the Application"),
	                          @ApiResponse(code = 400, message = "You are passed null parameters or Invalid parameters"),
	                          @ApiResponse(code = 401, message = "Invalid credentials"),
	                          @ApiResponse(code = 403, message = "Insufficient privileges.") })
	  @PostMapping(path = "/", produces = { APPLICATION_JSON_VALUE })
	  @ApiImplicitParams ({ @ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en"),
			  @ApiImplicitParam (name = "email", value = "Login ID", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "xyz@abc.com"),
			  @ApiImplicitParam (name = "password", value = "Password", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Password123456"),
			  @ApiImplicitParam (name = "tenantID", value = "tenantID", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Tenant001")})
	  public ResponseEntity<LoginResponse> authenticateUser(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, HttpServletRequest request)
	  {
		 String emailID = request.getHeader("email"); 
		 String password = request.getHeader("password");
		 String tenantID = request.getHeader("tenantID");
		 
		 LoginRequest loginRequest = new LoginRequest(emailID, password, tenantID);
		 
	     return loginService.authenticateUser(locale, loginRequest);
	  }

  


}


