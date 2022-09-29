package com.datafoundry.loginUserService.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.datafoundry.loginUserService.LoginUserServiceApplication;
import com.datafoundry.loginUserService.model.AuditCollection;
import com.datafoundry.loginUserService.model.GenerateToken;
import com.datafoundry.loginUserService.model.RoleCollection;
import com.datafoundry.loginUserService.model.RoleEntity;
import com.datafoundry.loginUserService.model.SecurityQuestionsCollection;
import com.datafoundry.loginUserService.model.UserCollection;
import com.datafoundry.loginUserService.model.UserEntity;
import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.request.RoleCollectionRequest;
import com.datafoundry.loginUserService.model.request.RoleEntityRequest;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserCollectionRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.repository.AuditCollectionRepository;
import com.datafoundry.loginUserService.repository.RoleCollectionRepository;
import com.datafoundry.loginUserService.repository.SecurityQuestionsCollectionRepository;
import com.datafoundry.loginUserService.repository.UserCollectionRepository;
import com.datafoundry.loginUserService.service.GenerateTokenService;
import com.google.gson.Gson;

@Component
public class UtilMongo {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    GenerateTokenService generateTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserCollectionRepository userCollectionRepository;

    @Autowired
    RoleCollectionRepository roleCollectionRepository;
    
    @Autowired
    SecurityQuestionsCollectionRepository secQuestionRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    
    @Autowired
    AuditCollectionRepository auditCollectionRepo;
    
    @Value("${allowedUnsuccessfulAttempts}")
    int allowedUnsuccessfulAttempts;
    
    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);
    
    public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(SecurityQuestionsRequest securityQuestionsRequest, String serviceName)
    {
    	logger.info("Starting to validate the security questions for the user "+securityQuestionsRequest.getEmailId());
    	
    	//Retrieve the user details
    	UserCollection userCollection = userCollectionRepository.findByEmailId(securityQuestionsRequest.getEmailId());    	
    	   	
    	//Proceed only if you find a match
    	if(userCollection!=null)
    	{
    		String userCollectionOldValue = userCollection.toString();
    		logger.info("Found a matching user collection");
    		//Validate Client ID and User ID too for an exact match.
    		if(userCollection.getClientId().equals(securityQuestionsRequest.getClientId()))
    		{
    			logger.info("Validated the user id and client id too. Now going to validate the security questions");
    			boolean match = false;
    			
    			//Retrieve the corresponding security questions associated with this user.
    			SecurityQuestionsCollection secQuestions = secQuestionRepo.findByEmailId(securityQuestionsRequest.getEmailId());
    			
    			//Compare the db values with the ones supplied from UI.    			
    			match = (securityQuestionsRequest.getSecurityQuestion1().equals(secQuestions.getSecurityQuestion1())) &&
    					(securityQuestionsRequest.getSecurityQuestion2().equals(secQuestions.getSecurityQuestion2())) &&
    					(securityQuestionsRequest.getSecurityQuestion3().equals(secQuestions.getSecurityQuestion3())) &&
    					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer1(), secQuestions.getSecurityAnswer1())) &&
    					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer2(), secQuestions.getSecurityAnswer2())) &&
    					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer3(), secQuestions.getSecurityAnswer3()));
    			
    			if(match)
    			{
    				//Unlock the user profile and reset the unsuccessful attempts counter
    				userCollection.setProfileLocked(false);
    				userCollection.setUnsuccessfulAttempts(0);
    				userCollectionRepository.save(userCollection);
    				
    				logger.info("Security questions validation successful, method returning now");
    				logAuditTrail(userCollectionOldValue, userCollection.toString(), securityQuestionsRequest.getEmailId(), securityQuestionsRequest.getClientId(), "SYSTEM", serviceName+" - Success", "Update");
    				return ResponseEntity.ok(new SecurityQuestionsResponse("Security questions are validated successfully. User profile has been unlocked!",true, 200));
    			}    			
    		}    		
    	}   
    	
    	logger.info("Security questions validation failed, method returning now");
    	logAuditTrail("", "", securityQuestionsRequest.getEmailId(), securityQuestionsRequest.getClientId(), "SYSTEM", serviceName+" - Failure", "Update");
    	return ResponseEntity.ok(new SecurityQuestionsResponse("Security questions validation failed.",false, 400));    	
    }
    
    public boolean existsByEmailId(String emailID)
    {
    	return userCollectionRepository.existsByEmailId(emailID);
    }
    
    public void logAuditTrail(String oldValue, String newValue, String emailID, String tenantID, String roleID, String serviceName, String operation )
    {
    	logger.info("Logging audit trail for "+emailID+", operation is "+operation);
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authToken.getAuthorities();
		String modifiedByUser = authToken.getName();
		String diff = "";
		
		if("Update".equals(operation))
		{
			diff = CommonUtils.difference(oldValue, newValue);
		}
		
		AuditCollection audit = new AuditCollection();   	
		
		if(authorities != null)
		{
			logger.info("User role is "+authorities.get(0).getAuthority());
			audit.setModifiedUserRole(authorities.get(0).getAuthority());
		}
		else
		{
			logger.info("No user role found, setting it to ANONYMOUS");
			audit.setModifiedUserRole("");
		}
		
		audit.setModifiedBy(modifiedByUser);
		audit.setModifiedDateTime(new Date(System.currentTimeMillis()));
		audit.setOperation(operation);
		audit.setServiceName(serviceName);
		audit.setEmailID(emailID);
		audit.setTenantID(tenantID);
		
		if("Create".equals(operation))
		{  
			logger.info("Setting the create data");
			audit.setCreatedData(newValue);
			audit.setDroppedData(null);
			audit.setUpdatedData(null);
		} 
		else if("Delete".equals(operation))
		{
			logger.info("Setting the delete data");
			audit.setCreatedData(null);
			audit.setDroppedData(oldValue.toString());
			audit.setUpdatedData(null);
		}
		else if("Update".equals(operation))
		{
			logger.info("Setting the updated data");
			audit.setCreatedData(null);
			audit.setDroppedData(null);
			audit.setUpdatedData(diff);
		}
		
		auditCollectionRepo.save(audit);  	
		logger.info("Logging audit trail for "+emailID+" complete.");
	}	

    public ResponseEntity<UserResponse> updateUserProfile(String emailID, String clientID, UserCollectionRequest userRequest, String serviceName) 
    {
    	logger.info("Strating the method updateUserProfile(NoSQL)");
    	
        UserCollection userCollection = userCollectionRepository.findByEmailId(emailID);        
        
        if(userCollection!=null)
        {
        	logger.info("User exists, proceeding to update the fields as per the values supplied from request");
        	String oldUserCollectionValue = userCollection.toString();
        	userCollection.setFirstName(userRequest.getFirstName());
        	userCollection.setLastName(userRequest.getLastName());
        	userCollection.setManager(userRequest.getManager());
            userCollection.setSubdomain(userRequest.getSubdomain());
            userCollection.setStatus(userRequest.getStatus());       
	        		
	        userCollectionRepository.save(userCollection);
	        
	        logger.info("User profile saved. Going to update the security questions");
	        
	        SecurityQuestionsCollection secQuestionCollection = secQuestionRepo.findByEmailId(emailID);
	        String oldSecQuestCollectionValue = secQuestionCollection.toString();
	        secQuestionCollection.setSecurityQuestion1(userRequest.getSecurityQuestion1());
	        secQuestionCollection.setSecurityQuestion2(userRequest.getSecurityQuestion2());
	        secQuestionCollection.setSecurityQuestion3(userRequest.getSecurityQuestion3());
	        secQuestionCollection.setSecurityAnswer1(passwordEncoder.encode(userRequest.getSecurityAnswer1()));
	        secQuestionCollection.setSecurityAnswer2(passwordEncoder.encode(userRequest.getSecurityAnswer2()));
	        secQuestionCollection.setSecurityAnswer3(passwordEncoder.encode(userRequest.getSecurityAnswer3()));
	        
	        logger.info("Security questions have been updated. About to log the audit trail for both user profile and security questions");
	        
	        logAuditTrail(oldUserCollectionValue, userCollection.toString(),emailID, clientID, "", serviceName, "Update");
	        logAuditTrail(oldSecQuestCollectionValue, secQuestionCollection.toString(),emailID, clientID, "", serviceName, "Update");
	        
	        logger.info("Successfully updated the user profile. Returning from the method updateUserProfile(NoSQL)");
	        return ResponseEntity.ok(new UserResponse( true, 200, "User profile for "+emailID+"  has been successfully updated"));
        }
        
        logger.error("Could not find the user. Nothing tou update. Returning from updateUserProfile(NoSQL)");
        return ResponseEntity.ok(new UserResponse( true, 404, "Bad request, user information could not be found"));
    }

    
    public ResponseEntity<UserResponse>  saveUserProfile(UserCollectionRequest userCollectionRequest, String serviceName) 
    {
    	logger.info("Starting to save user profile for "+userCollectionRequest.getEmail());
    	
    	if(!userCollectionRepository.existsByEmailId(userCollectionRequest.getEmail()))
	    {
	    	
	    	UserCollection userCollection = new UserCollection();
	    	userCollection.setEmailId(userCollectionRequest.getEmail());
	    	userCollection.setClientId(userCollectionRequest.getClientId());
	    	userCollection.setUserId(userCollectionRequest.getUserId());
	    	userCollection.setRoleId(userCollectionRequest.getRoleId());
	    	userCollection.setPassword(passwordEncoder.encode(userCollectionRequest.getPassword()));
	    	userCollection.setSubdomain(userCollectionRequest.getSubdomain());
	    	userCollection.setStatus(userCollectionRequest.getStatus());
	    	userCollection.setCreatedBy(userCollectionRequest.getCreatedBy());
	    	userCollection.setCreatedTime(new Date(System.currentTimeMillis()));
	    	userCollection.setUpdateBy(userCollectionRequest.getUpdateBy());
	    	userCollection.setUpdatedTime(new Date(System.currentTimeMillis()));
	    	userCollection.setLastLoginTime(null);
	    	userCollection.setName(userCollectionRequest.getFirstName() +" "+ userCollectionRequest.getLastName());
	    	userCollection.setManager(userCollectionRequest.getManager());
	    	userCollection.setRoleName(userCollectionRequest.getRoleName());
	    	userCollection.setDepartment(userCollectionRequest.getDepartment());
	    	userCollection.setDesignation(userCollectionRequest.getDesignation());
	    	userCollection.setMobileNo(userCollectionRequest.getMobileNo());
	    	userCollection.setUserPicURL(userCollectionRequest.getUserPicURL());
	    	userCollection.setUnsuccessfulAttempts(0);
	    	userCollection.setLocale(userCollectionRequest.getLocale());
	    	userCollection.setFirstName(userCollectionRequest.getFirstName());
	    	userCollection.setLastName(userCollectionRequest.getLastName());
	
	        userCollectionRepository.save(userCollection);
	        
	        SecurityQuestionsCollection secQuestionCollection = new SecurityQuestionsCollection(userCollectionRequest.getEmail(), userCollectionRequest.getClientId(), 
	        		userCollectionRequest.getUserId(), userCollectionRequest.getSecurityQuestion1(), passwordEncoder.encode(userCollectionRequest.getSecurityAnswer1()), 
	        		userCollectionRequest.getSecurityQuestion2(), passwordEncoder.encode(userCollectionRequest.getSecurityAnswer2()), userCollectionRequest.getSecurityQuestion3(), 
	        		passwordEncoder.encode(userCollectionRequest.getSecurityAnswer3()));
	        
	        secQuestionRepo.save(secQuestionCollection);
	        
		    logger.info("Saved the user and security questions collections, about to log the audit trail.");    
	        logAuditTrail("", userCollection.toString(), userCollectionRequest.getEmail(), userCollectionRequest.getClientId(), userCollectionRequest.getRoleId(), serviceName, "Create");
	        logAuditTrail("", secQuestionCollection.toString(), userCollectionRequest.getEmail(), userCollectionRequest.getClientId(), userCollectionRequest.getRoleId(), serviceName, "Create");
	        
	    	logger.info("Successfully completed creating the user profile.");
	        return ResponseEntity.ok(new UserResponse( true, 200, "User "+userCollectionRequest.getEmail()+" has been successfully registered"));
	    }
    	else
    	{
    		logger.error("User with email id "+userCollectionRequest.getEmail()+" already exists. Exiting saveUserProfile.");
    		return ResponseEntity.ok(new UserResponse( false, 400, "User ID already exists."));
    	}
    	
    }

    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) 
    {
    	logger.info("Started the method authenticateUser(NoSQL) for "+loginRequest.getEmail());
        UserCollection userCollection = userCollectionRepository.findByEmailId(loginRequest.getEmail());
        
        if(userCollection != null) {
        	
        	//Save the current user details into a string, for audit purpose.
        	String userCollectionOldValue = userCollection.toString();
        	
        	//Proceed only if the profile is not locked.
        	if(!userCollection.isProfileLocked())
        	{
        		//Retrieve the role associated with this user
	        	RoleCollection roleCollection = roleCollectionRepository.findByRoleId(userCollection.getRoleId());
	        	try {
	        		//Authenticate against the db with the supplied user name and password
	        		authenticationManager.authenticate(
	                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	        	}
	        	catch(org.springframework.security.authentication.BadCredentialsException bce)
	        	{
	        		//Exception is thrown when the credentials are incorrect. Log it as an unsuccessful attempt and proceed.
	        		int unsuccessfulAttempts = userCollection.getUnsuccessfulAttempts()+1;
	        		if(unsuccessfulAttempts <= allowedUnsuccessfulAttempts)
	        		{
	        			userCollection.setUnsuccessfulAttempts(unsuccessfulAttempts);	        			
	        		}
	        		else
	        		{
	        			//Lock the profile if the number of unsuccessful attempts is more than three.
	        			logger.error("More than three unsuccessful attempts for the user "+userCollection.getEmailId()+", locking the profile. The user can unlock answering the security questions.");
	        			userCollection.setProfileLocked(true);
	        		}
	        		
	        		userCollectionRepository.save(userCollection);
	        		
	        		//Save audit trail of unsuccessful login
	        		logAuditTrail(userCollectionOldValue, userCollection.toString(), userCollection.getEmailId(), userCollection.getClientId(), userCollection.getRoleId(), "Login Service", "Update");
	     	    	
	        		//Rethrow the exception
	        		throw bce;
	        	}
	        	
	
	            String token = jwtUtils.generateToken(loginRequest.getEmail());
	            GenerateToken generatedToken = new GenerateToken(token,  loginRequest.getEmail());
	            generateTokenService.save(generatedToken);
	            
	            UUID uuid = UUID.randomUUID();
	            
	            String roleData = roleCollection !=null ? new Gson().toJson(roleCollection) : "{}";
	            String cacheEntry = "{ \"userId\":\""+userCollection.getUserId()+" \","
	            		+ "\"sessionToken\" : \""+token+"\","
	            		+ "\"createdTime\":\""+new Date(System.currentTimeMillis())+"\","
	            		+ "\"subdomain\":\""+userCollection.getSubdomain()[0]+"\","
	    				+ "\"data\":{\"userData\":"+new Gson().toJson(userCollection)+","
						+ "\"roleData\":"+roleData+"}";
	                        
	            redisTemplate.opsForHash().put("Login", uuid.toString(), cacheEntry);
	            redisTemplate.expire("Login",900, TimeUnit.SECONDS);
	            
	            userCollection.setLastLoginTime(new Date(System.currentTimeMillis()));
	            userCollection.setUnsuccessfulAttempts(0);
	            userCollectionRepository.save(userCollection);
	            
	            //Save audit trail of successful login
	            logAuditTrail(userCollectionOldValue, userCollection.toString(), userCollection.getEmailId(), userCollection.getClientId(), userCollection.getRoleId(), "Login Service", "Update");
     	    		            
	            return ResponseEntity.ok(new LoginResponse("", uuid.toString(), true, 200, generatedToken));
        	}
        	else
        	{
            	logger.error("User account "+loginRequest.getEmail()+" is locked");
            	LoginResponse loginResponse = new LoginResponse("Cant login, user profile is locked.", "", false, 400, null);
                return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        	}
        } 
        else 
        {
        	logger.error("Email "+loginRequest.getEmail()+" doesnt exist");
        	LoginResponse loginResponse = new LoginResponse("Invalid credentials", "", false, 400, null);
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<UserResponse> assignRolesToUser(String roleID, String emailID, String tenantID, String serviceName)
    {
        logger.info("Starting the method assignRolesToUser");
        
        //Check if the user exists.
        UserCollection userCollection = userCollectionRepository.findByEmailId(emailID);
        String userCollectionOldValue = userCollection.toString();
        if(userCollection != null)
        {        	
        	//Fetch the role details.
        	RoleCollection roleCollection = roleCollectionRepository.findByRoleId(roleID);
        	if(roleCollection != null)
        	{
        		userCollection.setRoleId(roleCollection.getRoleId());
        		userCollection.setRoleName(roleCollection.getRoleName());
        		userCollectionRepository.save(userCollection);
        		logger.info("Ending the method assignRolesToUser with success");
        		
        		logAuditTrail(userCollectionOldValue, userCollection.toString(), emailID, userCollection.getClientId(), roleID, serviceName, "Update");
        		return ResponseEntity.ok(new UserResponse( true, 200, "Role(s) assigned successfully to the user "+emailID));
        	}
        }
        logger.info("Ending the method assignRolesToUser with failure");
        return ResponseEntity.ok(new UserResponse(false, 400, "Invalid data. Role could not be assigned to the user "+emailID));
    }
    
    public ResponseEntity<UserResponse> revokeRolesFromUser(String roleId, String emailID, String tenantID, String serviceName)
    {
    	logger.info("***************************** Starting the method revokeRolesFromUser "+emailID);
        
        //Check if the user exists.
        UserCollection userCollection = userCollectionRepository.findByEmailId(emailID);

        if(userCollection != null)
        {   
            String userCollectionOldValue = userCollection.toString();
        	//Fetch the role details.
        	RoleCollection roleCollection = roleCollectionRepository.findByRoleId(roleId);
        	if(roleCollection != null)
        	{
        		userCollection.setRoleId("0");
        		userCollection.setRoleName("None");
        		userCollectionRepository.save(userCollection);
        		logger.info("Ending the method revokeRolesFromUser with success");
        		
        		logAuditTrail(userCollectionOldValue, userCollection.toString(), emailID, userCollection.getClientId(), roleId, serviceName, "Update");
        		return ResponseEntity.ok(new UserResponse( true, 200, "Role revoked successfully from the user "+emailID));
        	}
        }
        logger.info("Ending the method revokeRolesFromUser with failure");
        return ResponseEntity.ok(new UserResponse(false, 400, "Invalid data. Role could not be revoked from the user "+emailID));
    }
    
    public ResponseEntity<UserResponse> findAllUsers(){
        List<UserCollection> user = userCollectionRepository.findAll();
        return ResponseEntity.ok(new UserResponse( true, 200, user));
    }

    public ResponseEntity<UserResponse> findUserByEmailID(String email){
        UserCollection user = userCollectionRepository.findByEmailId(email);
        return ResponseEntity.ok(new UserResponse( true, 200, user));
    }
    
    public ResponseEntity<UserResponse> changePassword(String emailID, String oldPassword, String newPassword, String serviceName)
    {
    	logger.info("Starting changePassword method");
        //Find the user with the given emailID.
    	UserCollection userCollection = userCollectionRepository.findByEmailId(emailID);
    	
    	//Proceed only if the user exists.
        if(userCollection != null) 
        {
        	String oldUserCollectionValue = userCollection.toString();
        	logger.info("Found the user, going to authenticate with old password");
        	//First authenticate with the old password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailID, oldPassword));
            
            logger.info("Authentication successful, going to update the password now");
            //Update the password only if the authentication succeeds.
            userCollection.setPassword(passwordEncoder.encode(newPassword));
            userCollectionRepository.save(userCollection);
            logger.info("Password changed successfully. Returning from the method now.");
            logAuditTrail(oldUserCollectionValue, userCollection.toString(), emailID, userCollection.getClientId(), "SYSTEM", serviceName, "Update");
            return ResponseEntity.ok(new UserResponse( true, 200, userCollection));
            
        }else {
        	logger.error("User not found. Returning from the method without updating the password");
               UserResponse userResponse = new UserResponse("Invalid user", false, 400);
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }       
        
    }

    public ResponseEntity<UserResponse> findAllRoles(){
        List<RoleCollection> roles = roleCollectionRepository.findAll();
        return ResponseEntity.ok(new UserResponse( true, 200, roles));
    }

    public ResponseEntity<UserResponse> findRoleByName(String email){
        RoleCollection role = roleCollectionRepository.findByRoleName(email);
        return ResponseEntity.ok(new UserResponse( true, 200, role));
    }
    
    public ResponseEntity<RoleResponse> createRole(RoleCollectionRequest roleRequest, String serviceName) 
    {
    	logger.info("Entering the method saveRole(RoleCollectionRequest)");
    	
    	logger.info("Generating UUID as role id");
    	String roleID = UUID.randomUUID().toString();
    	
    	logger.info("Creating a new role collection and setting the necessary details");
    	RoleCollection roleCollection = new RoleCollection();
    	roleCollection.setRoleId(roleID);
    	roleCollection.setRoleName(roleRequest.getRoleName());
    	roleCollection.setClientId(roleRequest.getClientId());
    	roleCollection.setRoleDisplayName(roleRequest.getRoleDisplayName());
    	roleCollection.setStatus(roleRequest.getStatus());
    	roleCollection.setCreatedBy(roleRequest.getCreatedBy());
    	roleCollection.setCreatedTime(new Date(System.currentTimeMillis()));
    	roleCollection.setUpdatedBy(roleRequest.getUpdatedBy());
    	roleCollection.setUpdatedTime(new Date(System.currentTimeMillis()));
    	roleCollection.setPermissions(roleRequest.getPermissions());
    	
    	logger.info("Saving the role collection");
    	roleCollectionRepository.save(roleCollection);
    	logAuditTrail("", roleCollection.toString(), "", "", "", serviceName, "Create");
    	logger.info("Returning from the method saveRole(RoleCollectionRequest)");
    	return ResponseEntity.ok(new RoleResponse( "Role "+roleRequest.getRoleName()+" successfully created", true, 200, null)); 
    }
    
    public ResponseEntity<RoleResponse> updateRole(String roleID, RoleCollectionRequest roleCollectionRequest, String serviceName) 
    {
    	RoleCollection roleCollection = roleCollectionRepository.findByRoleId(roleID);    	
    	if(roleCollection != null)
    	{
    		String oldRoleCollectionValue = roleCollection.toString();
    		roleCollection.setRoleDisplayName(roleCollectionRequest.getRoleDisplayName());
    		roleCollection.setStatus(roleCollectionRequest.getStatus());
    		roleCollection.setPermissions(roleCollectionRequest.getPermissions());
    		roleCollection.setUpdatedBy(roleCollectionRequest.getUpdatedBy());
    		roleCollection.setUpdatedTime(new Date(System.currentTimeMillis()));
    		
    		roleCollectionRepository.save(roleCollection);
        	
        	logAuditTrail(oldRoleCollectionValue, roleCollection.toString(), "", roleCollectionRequest.getClientId(),  roleCollectionRequest.getRoleName(), serviceName, "Update");
        	
        	return ResponseEntity.ok(new RoleResponse( "Role "+roleCollectionRequest.getRoleName()+" successfully created", true, 200, null ));
    	}
    	
    	return ResponseEntity.ok(new RoleResponse( "Bad request, role information could not be found",true, 404,null));

    }
    
    public ResponseEntity<RoleResponse> deleteRole(String roleID, String serviceName) 
    {
		RoleCollection roleCollection = roleCollectionRepository.findByRoleId(roleID);
		
		roleCollectionRepository.delete(roleCollection);
    	
		logAuditTrail(roleCollection.toString(), "", "", roleCollection.getClientId(),  roleCollection.getRoleName(), serviceName, "Delete");
    	
    	return ResponseEntity.ok(new RoleResponse( "Role "+roleCollection.getRoleName()+" successfully deleted", true, 200, roleCollection));
    }
}
