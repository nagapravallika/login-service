package com.datafoundry.loginUserService.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datafoundry.loginUserService.model.UserEntity;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.repository.UserEntityRepository;
import com.datafoundry.loginUserService.service.GenerateTokenService;
import com.datafoundry.loginUserService.service.LoginService;
import com.datafoundry.loginUserService.service.MyUserDetails;
import com.datafoundry.loginUserService.service.UserService;
import com.datafoundry.loginUserService.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/authservice/v1/users")
@Api(value = "UserController", tags = { "User APIs" })
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserEntityRepository userEntityRepository;
	
	@Autowired
	LoginService loginService;

	@Autowired
	GenerateTokenService generateTokenService;
	
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    
    @Autowired
    JwtUtils jwtUtils;

	
	@ApiOperation(value = "Common API to update the user password or to toggle the user profile status (Active to Inactive or vice versa). Updating the user profile requires the user authorization token. Toggling the status needs Admin permissions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the user password / Profile status changed successfully"),
			                 @ApiResponse(code = 400, message = "Not found - The UserID not found") })
	@PatchMapping(path = "/{userID}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParams({ @ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en"),
			@ApiImplicitParam (name = "oldPassword", value = "old Password", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Xyu234@3y"),
			@ApiImplicitParam (name = "newPassword", value = "new Password", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Awe$1234")})
	public ResponseEntity<UserResponse> changePasswordOrDeactivate(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale , @PathVariable(required = true) String userID, HttpServletRequest request)
	{
		String oldPassword = request.getHeader("oldPassword");
		String newPassword = request.getHeader("newPassword");
		String profileStatus = request.getHeader("profileStatus");
		
		if(profileStatus != null)
		{
			//Get the token from header and extract userID from it
			String token = request.getHeader("Authorization");
			String userIDFromToken = jwtUtils.getUsername(token);
			
			//Find the user entity belonging to the token.
			UserEntity userEntity = userEntityRepository.findByEmail(userIDFromToken);
			
			//Build user details (to fetch the roles associated with the user)
			MyUserDetails userDetails = MyUserDetails.build(userEntity);
			List<GrantedAuthority> authorities = (List<GrantedAuthority>)userDetails.getAuthorities();
			
			boolean isAdmin = false;
			
			//Check if this user has admin role
			for(int i=0; i<authorities.size(); i++)
			{
				GrantedAuthority authority = authorities.get(i);
				isAdmin = "superAdmin".equals(authority.getAuthority());
			}
			
			//Allow only if the user is admin
			if(isAdmin)
			{
				return userService.toggleStatus(locale, userID, profileStatus);
			}
			else
			{
				return ResponseEntity.ok(new UserResponse("Request unauthorized.", false, 403));
			}
		}
		
		else if(oldPassword !=null && newPassword != null)
		{
			return userService.changePassword(locale, userID, oldPassword, newPassword,"Change Password Service");
		}
		
		return ResponseEntity.ok(new UserResponse("Bad request. You must pass either old and new password combination or user status (not both at once)", false, 400));
		
	}
	
	@ApiOperation(value = "Validate security questions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully validated the security questions"),
			                 @ApiResponse(code = 400, message = "Security questions validation failed") })
	@PostMapping(path = "/{userID}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParams({@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en"),
	@ApiImplicitParam (name = "payload", value = "validate security questions", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "{\"tenantID\": \"tenant008\",\"securityQuestion1\" : \"What is your pet name?\",\"securityAnswer1\" : \"Tata\", \"securityQuestion2\" : \"What is your place of birth?\",\"securityAnswer2\" : \"Guntur\",\"securityQuestion3\" : \"What is your first job?\",\"securityAnswer3\" : \"SIS\"}")})
	public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String userID,HttpServletRequest request)
	{
		String payload = request.getHeader("payload");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModules(new JavaTimeModule());
		ObjectReader objectReader= objectMapper.reader();
		Map<String, String> data = null;
		
		try 
		{
			data = objectReader.forType(Map.class).readValue(payload);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok(new SecurityQuestionsResponse("Error retrieving the details from header. Failed to invalidate the session.", false, 400));
		}
		
		if(data != null)
		{
			String tenantID = data.get("tenantID");
			String securityQuestion1 = data.get("securityQuestion1");
			String securityQuestion2 = data.get("securityQuestion2");
			String securityQuestion3 = data.get("securityQuestion3");
			String securityAnswer1 = data.get("securityAnswer1");
			String securityAnswer2 = data.get("securityAnswer2");
			String securityAnswer3 = data.get("securityAnswer3");
			SecurityQuestionsRequest secQuestRequest = new SecurityQuestionsRequest(userID, tenantID, securityQuestion1, securityAnswer1,
					securityQuestion2,securityAnswer2,securityQuestion3,securityAnswer3);
			return userService.validateSecurityQuestions(locale, secQuestRequest);
		}

		return ResponseEntity.ok(new SecurityQuestionsResponse("Error retrieving the details from header. Failed to invalidate the session.", false, 400));
	}

	@ApiOperation(value = "Gets the details of a single user")
	@ApiParam(name = "userID", type="String")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrived the user details with the given ID"),
							@ApiResponse(code = 400, message = "Not found - The data not found"),
							@ApiResponse(code = 401, message = "Invalid parameters"),
							@ApiResponse(code = 403, message = "Insufficient privileges.") })
	@GetMapping(path = "/{userID}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<UserResponse> getOne( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale,@PathVariable(required = true) String userID) {
		return userService.findByEmailID(locale,userID);
	}

	@ApiOperation(value = "Gets all user details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrived all the user details"),
							@ApiResponse(code = 400, message = "Not found - The data not found"),
							@ApiResponse(code = 401, message = "Invalid parameters"),
							@ApiResponse(code = 403, message = "Insufficient privileges.") })
	@GetMapping(path = "/", produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<UserResponse> getAll( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale) {
		return userService.findAll(locale);
	}


	@ApiOperation(value = "Updates the user profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully assigned the role to the user"),
							@ApiResponse(code = 400, message = "Not found - The data not found"),
							@ApiResponse(code = 401, message = "Invalid parameters"),
							@ApiResponse(code = 403, message = "Insufficient privileges.") })
	@PutMapping(path = "/{emailID}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	//String emailID, String tenantID, UserRequest userRequest, String serviceName
	public ResponseEntity<UserResponse> updateUserProfile( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String emailID,  @RequestBody(required = true) UserEntityRequest userRequest, HttpServletRequest httpRequest) {
		String token = httpRequest.getHeader("Authorization");
		return userService.updateUserProfile(locale, emailID, token, userRequest);
	}

	@ApiOperation(value = "Assigns the specified role(s) to the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully assigned the role to the user"),
							@ApiResponse(code = 400, message = "User not found, could not assign the roles"),
							@ApiResponse(code = 403, message = "Insufficient privileges.") })
	@PatchMapping(path = "/{emailID}/roles/{roleNames}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<UserResponse> assignRolesToUser( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String emailID, @PathVariable(required = true) String[] roleNames) {
		 return userService.assignRolesToUser(locale, roleNames, emailID);
	}

	@ApiOperation(value = "Revokes the specified role(s) from the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully revoked the role(s) from the user"),
							@ApiResponse(code = 400, message = "User not found, could not revoke the roles."),
							@ApiResponse(code = 401, message = "Invalid parameters"),
							@ApiResponse(code = 403, message = "Insufficient privileges.") })
	@DeleteMapping(path = "/{emailID}/roles/{roleNames}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<UserResponse> revokeRolesFromUser( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String emailID, @PathVariable(required = true) String[] roleNames) {
		 return userService.revokeRolesFromUser(locale, roleNames, emailID);
	}

	@ApiOperation(value = "Logs out the current user and deletes the token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "You are successfully logout from the application"),
							@ApiResponse(code = 400, message = "Redirect to login page") })
	@DeleteMapping(path = "/{userID}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<UserResponse> deleteGenerateToken( @ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String userID) {
		return userService.logout(locale, userID);
	}

	 @ApiOperation (value ="Register a User")
	 @ApiResponses (value = { @ApiResponse(code = 200, message = "User registered successfully."),
							  @ApiResponse(code = 401, message = "Invalid parameters"),
			 				  @ApiResponse(code = 400, message = "User with given ID already exists.") })
	 @PostMapping (path = "/", consumes  = { APPLICATION_JSON_VALUE }, produces = { APPLICATION_JSON_VALUE })
	 @ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	 public ResponseEntity<UserResponse> register(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @Valid @RequestBody(required = true) UserEntityRequest userRequest) {

		 return userService.registerUser(locale, userRequest);
	 }

}
