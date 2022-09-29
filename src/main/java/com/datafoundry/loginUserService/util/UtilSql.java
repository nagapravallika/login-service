package com.datafoundry.loginUserService.util;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.datafoundry.loginUserService.LoginUserServiceApplication;
import com.datafoundry.loginUserService.model.AuditEntity;
import com.datafoundry.loginUserService.model.GenerateToken;
import com.datafoundry.loginUserService.model.PermissionEntity;
import com.datafoundry.loginUserService.model.RoleEntity;
import com.datafoundry.loginUserService.model.SecurityQuestionsEntity;
import com.datafoundry.loginUserService.model.UserEntity;
import com.datafoundry.loginUserService.model.request.LoginRequest;
import com.datafoundry.loginUserService.model.request.RoleEntityRequest;
import com.datafoundry.loginUserService.model.request.SecurityQuestionsRequest;
import com.datafoundry.loginUserService.model.request.UserEntityRequest;
import com.datafoundry.loginUserService.model.response.LoginResponse;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.SecurityQuestionsResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.repository.AuditEntityRepository;
import com.datafoundry.loginUserService.repository.PermissionEntityRepository;
import com.datafoundry.loginUserService.repository.RoleEntityRepository;
import com.datafoundry.loginUserService.repository.SecurityQuestionsEntityRepository;
import com.datafoundry.loginUserService.repository.UserEntityRepository;
import com.datafoundry.loginUserService.service.GenerateTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

@Component
public class UtilSql {


    @Autowired
    private UserEntityRepository userEntityRepository;
    
    @Autowired
    private RoleEntityRepository roleEntityRepository;    
    
    @Autowired
    private AuditEntityRepository auditEntityRepository;
    
    @Autowired
    private PermissionEntityRepository permissionEntityRepository;
    
    @Autowired
    private SecurityQuestionsEntityRepository secQuestRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GenerateTokenService generateTokenService;
    
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private JwtUtils jwtUtils;
    
	@Autowired
	private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Value("${allowedUnsuccessfulAttempts}")
    private int allowedUnsuccessfulAttempts;
    
    @Autowired   
    private MessageSource messageSource;  
    
    
    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);
    
    public ResponseEntity<SecurityQuestionsResponse> validateSecurityQuestions(Locale locale, SecurityQuestionsRequest securityQuestionsRequest, String serviceName)
    {
    	logger.debug("Entering the method validateSecurityQuestions(SQL)");
    	
    	//Retrieve the user details.
    	UserEntity userEntity = userEntityRepository.findByEmail(securityQuestionsRequest.getEmailId());
    	
    	SecurityQuestionsEntity secQuestEntity = secQuestRepo.findByEmailAndTenantID(securityQuestionsRequest.getEmailId(), securityQuestionsRequest.getClientId());
    	
    	//Proceed only if you find a match.
    	if(userEntity != null && secQuestEntity != null)
    	{
    		String userEntityOldValue = userEntity.toString();
    		if(userEntity.getTenantID().equals(securityQuestionsRequest.getClientId()) )
    		{
    			logger.debug("Validated the tenant id too. Now going to validate the security questions");
	    		boolean securityQuestionsMatch = false;
	    		
	    		//Compare the db values with the ones supplied in request.
	    		securityQuestionsMatch = (securityQuestionsRequest.getSecurityQuestion1().equals(secQuestEntity.getSecurityQuestion1())) &&
	    				 (securityQuestionsRequest.getSecurityQuestion2().equals(secQuestEntity.getSecurityQuestion2())) &&
	    				 (securityQuestionsRequest.getSecurityQuestion3().equals(secQuestEntity.getSecurityQuestion3())) &&
	 					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer1(), secQuestEntity.getSecurityAnswer1())) &&
	 					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer2(), secQuestEntity.getSecurityAnswer2())) &&
	 					(passwordEncoder.matches(securityQuestionsRequest.getSecurityAnswer3(), secQuestEntity.getSecurityAnswer3()));
	    		
	    		if(securityQuestionsMatch)
	    		{
					//Unlock the user profile and reset the unsuccessful attempts counter
	    			userEntity.setProfileLocked(false);
	    			userEntity.setUnsuccessfulAttempts(0);
					userEntityRepository.save(userEntity);
					
					logger.debug("Security questions validation successful, method returning now");
					logAuditTrail(userEntityOldValue, userEntity.toString(), securityQuestionsRequest.getEmailId(), securityQuestionsRequest.getClientId(), "SYSTEM", serviceName+" - Success", "Update");
					return ResponseEntity.ok(new SecurityQuestionsResponse(messageSource.getMessage("security.question.validation.success", null, locale),true, 200));
	    		}    
    		}
    	}
    	
		logger.error("Security questions validation failed, method returning now");
		logAuditTrail("", "", securityQuestionsRequest.getEmailId(), securityQuestionsRequest.getClientId(), "SYSTEM", serviceName+" - Failure", "Update");
		return ResponseEntity.ok(new SecurityQuestionsResponse(messageSource.getMessage("security.question.validation.failure", null, locale),false, 400)); 
    }
    
    public boolean existsByEmailId(Locale locale, String emailID)
    {
    	return userEntityRepository.existsByEmail(emailID);
    }
    
    public void logAuditTrail(String oldValue, String newValue, String emailID, String tenantID, String roleID, String serviceName, String operation )
    {
    	logger.debug("Starting method logAuditTrail(SQL)");
    	
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		List<GrantedAuthority> authorities = (List<GrantedAuthority>)authToken.getAuthorities();
		
		logger.debug("Retrieved the authorities list");
		
		String modifiedByUser = authToken.getName();
		String diff = "";
		if("Update".equals(operation))
		{
			logger.debug("Operation is update, so doing a diff now");
			diff = CommonUtils.difference(oldValue, newValue);
		}
		
		AuditEntity audit = new AuditEntity();   	
		
		if(authorities.size() > 0)
		{
			logger.debug("User role is "+authorities.get(0).getAuthority());
			audit.setModifiedUserRole(authorities.get(0).getAuthority());
		}
		else
		{
			logger.debug("No user role found, setting it to SYSTEM");
			audit.setModifiedUserRole("SYSTEM");
		}
		
		audit.setModifiedBy(modifiedByUser);
		audit.setModifiedDateTime(new Date(System.currentTimeMillis()));
		audit.setOperation(operation);
		audit.setServiceName(serviceName);
		audit.setEmailID(emailID);
		audit.setTenantID(tenantID);
		
		if("Create".equals(operation))
		{        	
			audit.setCreatedData(newValue);
			audit.setDroppedData(null);
			audit.setUpdatedData(null);
		} 
		else if("Delete".equals(operation))
		{
			audit.setCreatedData(null);
			audit.setDroppedData(oldValue.toString());
			audit.setUpdatedData(null);
		}
		else if("Update".equals(operation))
		{
			audit.setCreatedData(null);
			audit.setDroppedData(null);
			audit.setUpdatedData(diff);
		}
		
		logger.debug("All fields are applied, about to save the audit entry");
		
		auditEntityRepository.save(audit);  	
		
		logger.debug("Successfully completed logging audit, exiting the method logAuditTrail(SQL)");
	}
    
     
    public ResponseEntity<UserResponse> saveUserProfile(Locale locale, UserEntityRequest userEntityRequest, String serviceName) 
    {
    	logger.debug("Starting saveUserProfile(SQL) method");
    	String emailID = userEntityRequest.getEmail();
    	String tenantID = userEntityRequest.getTenantID();
    	UserEntity tempUser = userEntityRepository.findByEmail(emailID);
    	
    	//Proceed only if no other account exists with the same email id.
    	if(tempUser == null)
    	{
    		logger.debug("User doesnt exists, proceeding with profile creation");
    		//Create the user entity. Password and security answers are encrypted.
	        UserEntity userEntity = new UserEntity(emailID,
	                passwordEncoder.encode(userEntityRequest.getPassword()),
	                userEntityRequest.getFirstName(),
	        		userEntityRequest.getLastName(),
	        		emailID,
	        		tenantID,
	        		userEntityRequest.getManagerEmailID(),	        		
	        		"Active");
	        
	        SecurityQuestionsEntity secQuestEntity = new SecurityQuestionsEntity();
	        secQuestEntity.setEmail(emailID);
	        secQuestEntity.setTenantID(tenantID);
	        secQuestEntity.setSecurityQuestion1(userEntityRequest.getSecurityQuestion1());
	        secQuestEntity.setSecurityQuestion2(userEntityRequest.getSecurityQuestion2());
	        secQuestEntity.setSecurityQuestion3(userEntityRequest.getSecurityQuestion3());
	        secQuestEntity.setSecurityAnswer1(passwordEncoder.encode(userEntityRequest.getSecurityAnswer1()));
	        secQuestEntity.setSecurityAnswer2(passwordEncoder.encode(userEntityRequest.getSecurityAnswer2()));
	        secQuestEntity.setSecurityAnswer3(passwordEncoder.encode(userEntityRequest.getSecurityAnswer3()));

	        //secQuestRepo.save(secQuestEntity);
	        
	        userEntity.setSecurityQuestionsID(secQuestEntity.getId());
	        userEntity.setSecQuestions(secQuestEntity);
	        
	        Set<RoleEntity> userRoles = new HashSet<RoleEntity>();
	        
//	        String[] roleNames = userEntityRequest.getRoleNames();
//	        
//	        if(roleNames != null)
//		    {
//		        for(int i=0; i<roleNames.length; i++)
//	    		{
//		        	RoleEntity role = roleEntityRepository.findByRoleName(roleNames[i]);
//		        	if(role != null)
//		        	{
//		        		userRoles.add(role);
//		        	}
//		        }
//		    }
	             	
        	logger.debug("Retrieved the role information, about to set it on Userentity");
	
	        userEntity.setUserRoles(userRoles);
	        userEntity.setUnsuccessfulAttempts(0);
	        userEntity.setProfileLocked(false);
	        
	        userEntityRepository.save(userEntity);
	        logAuditTrail("", userEntity.toAuditString(),emailID, tenantID, "", serviceName, "Create");
	        
	        logger.debug("Saved the user profile and audit trails. Exiting saveUserProfile(SQL) method");
	        
	        return ResponseEntity.ok(new UserResponse( true, 200, messageSource.getMessage("save.user.profile.success", null, locale)+emailID));
    	}
    	else
    	{
    		logger.error("User already exists with the email id "+emailID+". Cannot create user profile. Exiting saveUserProfile(SQL) method");
    		return ResponseEntity.ok(new UserResponse( false, 400, messageSource.getMessage("save.user.profile.failure", null, locale)+emailID));
    	}
    }
    
    public boolean existsByEmailID(Locale locale, String emailID) 
    {
    	logger.debug("Starting the method existsByEmailID(SQL)");
        UserEntity userEntity = userEntityRepository.findByEmail(emailID);
        if(userEntity != null)
        {
        	logger.debug("User with "+emailID+" exists, returning true. Exiting the method existsByEmailID(SQL)");
        	return true;
        }
    	logger.debug("User with "+emailID+" doesnt exist, returning false. Exiting the method existsByEmailID(SQL)");
        return false;
    }
    
    public ResponseEntity<UserResponse> toggleStatus(Locale locale, String emailID, String status) 
    {
    	logger.debug("Starting the method deactivate with input "+emailID);
    	
        UserEntity userEntity = userEntityRepository.findByEmail(emailID);
        if(userEntity != null)
        {
        	String oldUserEntity = userEntity.toAuditString();
        	logger.debug("User with "+emailID+" exists. Going to set the profile as inactive");
        	userEntity.setStatus(status);
        	userEntityRepository.save(userEntity);
        	logAuditTrail(oldUserEntity, userEntity.toAuditString(),emailID, userEntity.getTenantID(), "", "Deactivate User", "Update");
        	return ResponseEntity.ok(new UserResponse( emailID+messageSource.getMessage("deactivate.user.success", null, locale), true, 200));        	
        }
        
    	logger.debug("User with "+emailID+" doesnt exist, returning from the method deactivate");
    	return ResponseEntity.ok(new UserResponse( messageSource.getMessage("deactivate.user.failure", null, locale)+emailID, false, 400));
    }
        
    
    public ResponseEntity<UserResponse> updateUserProfile(Locale locale, String emailID, UserEntityRequest userRequest, String token, String serviceName) 
    {
    	logger.debug("Starting the method updateUserProfile");
    	
			 UserDetails usr = this.userDetailsService.loadUserByUsername(emailID);
			 logger.debug("Retrieved the user details(with the supplied email ID "+emailID+") from userDetailsService");
			 
			 //do validation
			 boolean isValid = jwtUtils.validateToken(token,usr.getUsername());
			 logger.debug("Token validation result ->"+isValid);
			 
			 //Proceed only if the supplied user ID matches with the one in the token.
			 if(isValid)
			 {
		        UserEntity userEntity = userEntityRepository.findByEmail(emailID);        
		        
		        if(userEntity!=null)
		        {	        			            
		        	logger.debug("User found. Proceeding with updating the fields");
		        	String oldUserEntityValue = userEntity.toAuditString();
		        	userEntity.setFirstName(userRequest.getFirstName());
		            userEntity.setLastName(userRequest.getLastName());
		            userEntity.setManagerEmailID(userRequest.getManagerEmailID());
		            
		            SecurityQuestionsEntity secQuestEntity = secQuestRepo.findByEmailAndTenantID(emailID, userEntity.getTenantID());
		            
		
		            secQuestEntity.setSecurityQuestion1(userRequest.getSecurityQuestion1());
		            secQuestEntity.setSecurityAnswer1(passwordEncoder.encode(userRequest.getSecurityAnswer1()));
		            secQuestEntity.setSecurityQuestion2(userRequest.getSecurityQuestion2());
		            secQuestEntity.setSecurityAnswer2(passwordEncoder.encode(userRequest.getSecurityAnswer2()));
		            secQuestEntity.setSecurityQuestion3(userRequest.getSecurityQuestion3());
		            secQuestEntity.setSecurityAnswer3(passwordEncoder.encode(userRequest.getSecurityAnswer3()));
		            
		            secQuestRepo.save(secQuestEntity);
		            
		            userEntity.setSecQuestions(secQuestEntity);
			        		
			        userEntityRepository.save(userEntity);
			        
			        logger.debug("Updated the user profile successfully.");
			        logAuditTrail(oldUserEntityValue, userEntity.toAuditString(),emailID, userEntity.getTenantID(), "", serviceName, "Update");
			        
			        logger.debug("Updated the user profile and created the audit trail successfully. Exiting the method updateUserProfile");
			        return ResponseEntity.ok(new UserResponse( emailID+messageSource.getMessage("update.user.profile.success", null, locale), true, 200 ));
		        }
			 }
		
        logger.error("Could not find the user with email ID "+emailID+" Exiting the method updateUserProfile");
        return ResponseEntity.ok(new UserResponse( messageSource.getMessage("update.user.profile.failure", null, locale), true, 404 ));
    }
    
    public ResponseEntity<RoleResponse> createRole(Locale locale, RoleEntityRequest roleRequest, String serviceName) 
    {
    	logger.debug("Starting the method createRole(SQL)");
    	RoleEntity tempRole = roleEntityRepository.findByRoleName(roleRequest.getRoleName());
    	if(tempRole == null)
    	{
    		PermissionEntity[] permissions = roleRequest.getPermissions();
    		Set<PermissionEntity> permissionSet = new HashSet<PermissionEntity>();
    		for(int i=0; i<permissions.length; i++)
    		{
    			if(!permissionEntityRepository.existsById(permissions[i].getId()))
    			{
    				permissionEntityRepository.save(permissions[i]);
    			}
    			permissionSet.add(permissions[i]);
    		}
    		
	    	RoleEntity roleEntity = new RoleEntity( roleRequest.getTenantID(), roleRequest.getRoleName(), roleRequest.getRoleDisplayName(), roleRequest.getStatus(), roleRequest.getCreatedBy(), new Date(System.currentTimeMillis()), 
	    			roleRequest.getUpdatedBy(), new Date(System.currentTimeMillis()));
	    	
	    	roleEntity.setPermissions(permissionSet);
	    	
	    	roleEntityRepository.save(roleEntity);
	    	
	    	logAuditTrail("", roleEntity.toAuditString(), "", roleRequest.getTenantID(),  roleRequest.getRoleName(), serviceName, "Create");
	    	logger.debug("Role with name "+roleRequest.getRoleName()+" has been successfully created. Exiting the method createRole(SQL)");
	    	return ResponseEntity.ok(new RoleResponse(messageSource.getMessage("create.role.success", null, locale), true, 200,roleEntity ));
    	}
    	else
    	{
    		logger.error("Cannot create role. Another role with name "+roleRequest.getRoleName()+" already exists. Exiting the method createRole(SQL)");
    		return ResponseEntity.ok(new RoleResponse(messageSource.getMessage("create.role.failure", null, locale), false, 400,tempRole));
    	}
    		
    }
    
    public ResponseEntity<RoleResponse> updateRole(Locale locale, String roleName, RoleEntityRequest roleRequest, String serviceName) 
    {
    	logger.debug("Starting the method updateRole with role name "+roleName);
    	RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleName);    	
    	if(roleEntity != null)
    	{
    		logger.debug("Role entity found. Proceeding to update the fields");
    		String oldRoleEntityValue = roleEntity.toAuditString();
    		roleEntity.setRoleDisplayName(roleRequest.getRoleDisplayName());
    		roleEntity.setStatus(roleRequest.getStatus());
    		roleEntity.setUpdatedBy(roleRequest.getUpdatedBy());
    		roleEntity.setUpdatedTime(new Date(System.currentTimeMillis()));
    		
    		//roleRequest.getPermissions()
    		
    		roleEntityRepository.save(roleEntity);
        	
        	logAuditTrail(oldRoleEntityValue, roleEntity.toAuditString(), "", roleRequest.getTenantID(),  roleRequest.getRoleName(), serviceName, "Update");
        	logger.debug("Saved the role details and also created the audit trail. Returing from the method updateRole(SQL)");
        	return ResponseEntity.ok(new RoleResponse( messageSource.getMessage("update.role.success", null, locale), true, 200, roleEntity ));
    	}
    	
    	logger.error("Role details not found for "+roleName+". Returing from the method updateRole(SQL)");
    	return ResponseEntity.ok(new RoleResponse( messageSource.getMessage("update.role.failure", null, locale), false, 404, null));

    }
    
    public ResponseEntity<RoleResponse> deleteRole(Locale locale, String roleName, String serviceName) 
    {
    	logger.debug("Starting the method deleteRole");
		RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleName);
		if(roleEntity!=null)
		{
			logger.debug("Found the role, about to delete");
			roleEntityRepository.delete(roleEntity);
	    	
			logAuditTrail(roleEntity.toAuditString(), "", "", roleEntity.getTenantID(),  roleEntity.getRoleName(), serviceName, "Delete");
	    	
			logger.debug("Role deleted and audit trail created. Returning from the method deleteRole");
	    	return ResponseEntity.ok(new RoleResponse(messageSource.getMessage("delete.role.success", null, locale),  true, 200, roleEntity));
		}
		else
		{
			logger.error("Role "+roleName+" could not be found. Returning from the method deleteRole");
			return ResponseEntity.ok(new RoleResponse(messageSource.getMessage("delete.role.failure", null, locale), false, 400, null ));
		}
    }
	
    
    public ResponseEntity<UserResponse> assignRolesToUser(Locale locale, String[] roleNames, String emailID, String serviceName) 
    {
    	logger.debug("Starting the method assignRolesToUser");
        UserEntity userEntity = userEntityRepository.findByEmail(emailID);
        if(userEntity != null)
        {
        	String rolesFound = "";
        	String rolesNotFound = "";
        	logger.debug("user found, proceeding to assign roles");
	        String oldUserEntityValue = userEntity.toAuditString();
	        
	        Set<RoleEntity> newUserRoles = new HashSet<RoleEntity>();
	        for(int i=0; i<roleNames.length; i++)
	        {
		        RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleNames[i]);
		        if(roleEntity != null)
		        {
		        	rolesFound += " "+roleEntity.getRoleName();
		        	newUserRoles.add(roleEntity);
		        }
		        else
		        {
		        	rolesNotFound += " "+roleNames[i];
		        }
	        }
	        	
	        Set<RoleEntity> existingUserRoles = userEntity.getUserRoles();
	        existingUserRoles.addAll(newUserRoles);
	        
	        userEntity.setUserRoles(existingUserRoles);
	        
	        userEntityRepository.save(userEntity);
	        
	        logAuditTrail(oldUserEntityValue, userEntity.toAuditString(), emailID, userEntity.getTenantID(),  "", serviceName, "Update");
	
	        logger.info("Roles "+Arrays.toString(roleNames)+" successfully assigned to the user "+emailID+", returning from the method assignRolesToUser");
	        
	        String responseMessage = rolesFound.equals("")? "": rolesFound +" "+messageSource.getMessage("assign.roles.success", null, locale)+userEntity.getEmail()+"; ";
	        responseMessage += rolesNotFound.equals("")? "" : rolesNotFound+" "+messageSource.getMessage("assign.roles.partial.failure", null, locale);
	        return ResponseEntity.ok(new UserResponse(responseMessage, true, 200, null));
        }
        else
        {
        	logger.error("User with email ID "+emailID+" not found, returning from the method assignRolesToUser(SQL)");
        	return ResponseEntity.ok(new UserResponse( messageSource.getMessage("assign.roles.failure", null, locale), false, 400, null));
        }
    }

    public ResponseEntity<UserResponse> revokeRolesFromUser(Locale locale, String[] roleNames, String emailID, String serviceName) 
    {
    	logger.debug("Starting the method revokeRolesFromUser");
    	
    	//Find the user by email id.
        UserEntity userEntity = userEntityRepository.findByEmail(emailID);
        
        if(userEntity != null)
        {
        	String rolesFound = "";
        	String rolesNotFound = "";
        	logger.debug("user found, proceeding to revoke roles");
        	
	        String oldUserEntityValue = userEntity.toAuditString();
	        
	        Set<RoleEntity> currentUserRoles = userEntity.getUserRoles();
	        Set<RoleEntity> userRolesForRemoval = new HashSet<RoleEntity>();
	        
	        for(int i=0; i<roleNames.length; i++)
		    {
		        RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleNames[i]);
		        if(roleEntity != null)
		        {
		        	rolesFound += " "+roleEntity.getRoleName();
		        	userRolesForRemoval.add(roleEntity);
		        }		        
		        else
		        {
		        	rolesNotFound += " "+roleNames[i];
		        }
		    }
	        
	        //Remove the roles. 
	        currentUserRoles.removeAll(userRolesForRemoval);	        

        	logger.info("User found, proceeding to revoke the role");
        	
            
            userEntity.setUserRoles(currentUserRoles);	
            
            //Save the entity
            userEntityRepository.save(userEntity);
            
            logAuditTrail(oldUserEntityValue, userEntity.toAuditString(), emailID, userEntity.getTenantID(),  "", serviceName, "Update");
            
            String responseMessage = rolesFound.equals("")? "": rolesFound +" "+messageSource.getMessage("revoke.roles.success", null, locale)+userEntity.getEmail()+"; ";
	        responseMessage += rolesNotFound.equals("")? "" : rolesNotFound+" "+messageSource.getMessage("revoke.roles.partial.failure", null, locale);

            logger.info("Successfully revoked the roles "+Arrays.toString(roleNames)+" from the user "+emailID+". Returning from the method revokeRolesFromUser");
            return ResponseEntity.ok(new UserResponse( responseMessage, true, 200, null));

    
		        
        }
        
        logger.error("User with email ID "+emailID+" not found. Returning from the method revokeRolesFromUser");
        return ResponseEntity.ok(new UserResponse( messageSource.getMessage("revoke.roles.failure", null, locale)+emailID, false, 400, null));
        
    }
    
    public ResponseEntity<LoginResponse> authenticateUser(Locale locale, LoginRequest loginRequest) 
	{
        UserEntity userEntity = userEntityRepository.findByEmail(loginRequest.getEmail());
        logger.debug("starting authenticate user "+loginRequest.getEmail());
        if(userEntity != null) 
        {
        	if(userEntity.getStatus().equals("Inactive"))
        	{
        		LoginResponse loginResponse = new LoginResponse("Cant login, user profile is inactive.", "", false, 400, null);
        		return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        	}
        	
        	logger.info("Found the user and the profile is active");
        	//Save the current user details into a string, for audit purpose.
        	String userCollectionOldValue = userEntity.toAuditString();
        	
        	//Proceed only if the profile is not locked.
        	if(!userEntity.isProfileLocked())
        	{
        		logger.info("Profile is not locked");
        			        	
	        	try {
	        		//Authenticate against the db with the supplied user name and password
	        		authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	        	}
	        	catch(org.springframework.security.authentication.BadCredentialsException bce)
	        	{
	        		
	        		logger.info("exception in validating the credentials");
	        		//Exception is thrown when the credentials are incorrect. Log it as an unsuccessful attempt and proceed.
	        		int unsuccessfulAttempts = userEntity.getUnsuccessfulAttempts()+1;
	        		if(unsuccessfulAttempts <= allowedUnsuccessfulAttempts)
	        		{
	        			userEntity.setUnsuccessfulAttempts(unsuccessfulAttempts);	        			
	        		}
	        		else
	        		{
	        			//Lock the profile if the number of unsuccessful attempts is more than three.
	        			logger.error("More than three unsuccessful attempts for the user "+userEntity.getEmail()+", locking the profile. The user can unlock answering the security questions.");
	        			userEntity.setProfileLocked(true);
	        		}
	        		
	        		userEntityRepository.save(userEntity);
	        		
	        		//Save audit trail of unsuccessful login
	        		logAuditTrail(userCollectionOldValue, userEntity.toAuditString(), userEntity.getEmail(), userEntity.getTenantID(), "SYSTEM", "Login Service", "Update");
	     	    	
	        		//Rethrow the exception
	        		throw bce;
	        	}
	            String token = jwtUtils.generateToken(loginRequest.getEmail());
	            
	            
	            //Set the last login time on user entity after successful authentication
	            userEntity.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
	            
	            UUID uuid = UUID.randomUUID();
	            
	            Set<RoleEntity> userRoles = userEntity.getUserRoles();
	            String roleData = userRoles !=null ? new Gson().toJson(userRoles) : "{}";
		        String cacheEntry = "{ \"userId\":\""+userEntity.getEmail()+"\","
		            		+ "\"sessionToken\" : \""+token+"\","
		            		+ "\"createdTime\":\""+new Date(System.currentTimeMillis())+"\","
		    				+ "\"data\":{\"userData\":"+new Gson().toJson(userEntity)+","
							+ "\"roleData\":"+roleData+"}}";
		        
//	            redisTemplate.opsForHash().put("Login", userEntity.getEmail(), cacheEntry);
//	            redisTemplate.expire("Login",900, TimeUnit.SECONDS);   
		        
	            redisTemplate.opsForSet().add(userEntity.getEmail(), cacheEntry);
	            redisTemplate.expire(userEntity.getEmail(),900, TimeUnit.SECONDS);
	            
	            
		        GenerateToken generatedToken = new GenerateToken(token, userEntity.getEmail());
	            generateTokenService.save(generatedToken);
	            
	            userEntity.setLastLoginTime(new Date(System.currentTimeMillis()));
		        userEntity.setUnsuccessfulAttempts(0);
	            userEntityRepository.save(userEntity);
	            
            	//Save audit trail of successful login
	            logAuditTrail(userCollectionOldValue, userEntity.toAuditString(), userEntity.getEmail(), userEntity.getTenantID(), "SYSTEM", "Login Service", "Update");
	     	    		            
	            return ResponseEntity.ok(new LoginResponse(messageSource.getMessage("authenticate.user.success", null, locale), uuid.toString(), true, 200, generatedToken));
	        } 
        	else 
	        {
        		logger.error("User account "+loginRequest.getEmail()+" is locked");
            	LoginResponse loginResponse = new LoginResponse(messageSource.getMessage("authenticate.user.profile.locked", null, locale), "", false, 400, null);
                return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        	}
        } 
        else 
        {
        	logger.error("Email "+loginRequest.getEmail()+" doesnt exist");
            LoginResponse loginResponse = new LoginResponse(messageSource.getMessage("authenticate.user.failure", null, locale), "", false, 400, null);
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<UserResponse> findAllUsers(Locale locale){
        List<UserEntity> usersList = userEntityRepository.findAll();
        if(usersList.size() > 0)
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.all.users.success", null, locale), true, 200, usersList));
        }
        else
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.all.users.failure", null, locale), false, 400, null));
        }
    }

    public ResponseEntity<UserResponse> findUserByEmailID(Locale locale, String email){
        UserEntity userEntity = userEntityRepository.findByEmail(email);
        if(userEntity != null)
        {
        	return ResponseEntity.ok(new UserResponse( messageSource.getMessage("find.user.success", null, locale), true, 200, userEntity));
        }
        else
        {
        	return ResponseEntity.ok(new UserResponse( messageSource.getMessage("find.user.failure", null, locale), false, 200, null));
        }
    }
    
    public ResponseEntity<UserResponse> changePassword(Locale locale, String emailID, String oldPassword, String newPassword, String serviceName)
    {
    	//Find the user with the emailID.
    	UserEntity userEntity = userEntityRepository.findByEmail(emailID);
    	//Proceed only if the user exists.
        if(userEntity != null) {
        	
        	logger.debug(" proceeding as user entity is not null");

        	String oldUserEntityValue = userEntity.toAuditString();
        	//First authenticate with the old password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailID, oldPassword));
            
            //Update the password only if the authentication succeeds.
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntityRepository.save(userEntity);
            logAuditTrail(oldUserEntityValue.toString(), userEntity.toAuditString(), emailID, userEntity.getTenantID(),  "", serviceName, "Update");

            return ResponseEntity.ok(new UserResponse(messageSource.getMessage("change.password.success", null, locale), true, 200));
            
        }else {
            UserResponse userResponse = new UserResponse(messageSource.getMessage("change.password.failure", null, locale), false, 400);
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }       
        
    }
    
    public ResponseEntity<UserResponse> logout(Locale locale, String userID)
    {
    	String cacheValue = (String) redisTemplate.opsForSet().pop(userID);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModules(new JavaTimeModule());
		ObjectReader objectReader= objectMapper.reader();
		Map<String, String> data = null;
		
		try 
		{
			data = objectReader.forType(Map.class).readValue(cacheValue);
		}
		catch(Exception e)
		{
			return ResponseEntity.ok(new UserResponse(messageSource.getMessage("logout.cache.error", null, locale), false, 400));
		}
		
		if(data != null)
		{
			generateTokenService.deleteGenerateToken(data.get("sessionToken"));
	        redisTemplate.opsForSet().remove(userID,cacheValue);    
	        
			return ResponseEntity.ok(new UserResponse(messageSource.getMessage("logout.success", null, locale), true, 200));
		}
		
		return ResponseEntity.ok(new UserResponse(messageSource.getMessage("logout.failure", null, locale), false, 400));
    }
    
    public ResponseEntity<UserResponse> findAllRoles(Locale locale){
        List<RoleEntity> roleEntity = roleEntityRepository.findAll();
        if(roleEntity.size() > 0)
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.all.roles.success", null, locale), true, 200, roleEntity));
        }
        else
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.all.roles.failure", null, locale), true, 200, null));
        }
    }

    public ResponseEntity<UserResponse> findRoleByName(Locale locale, String roleName){
        RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleName);
        if(roleEntity != null)
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.role.success", null, locale), true, 200, roleEntity));
        }
        else
        {
        	return ResponseEntity.ok(new UserResponse(messageSource.getMessage("find.role.failure", null, locale), false, 400, null));
        }
    }


}
