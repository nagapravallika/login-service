package com.datafoundry.loginUserService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datafoundry.loginUserService.LoginUserServiceApplication;
import com.datafoundry.loginUserService.configuration.TenantAspectMongo;
import com.datafoundry.loginUserService.configuration.TenantAspectSql;
import com.datafoundry.loginUserService.model.UserCollection;
import com.datafoundry.loginUserService.model.UserEntity;
import com.datafoundry.loginUserService.repository.UserCollectionRepository;
import com.datafoundry.loginUserService.repository.UserEntityRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    DatabaseConnectionService dbConnection;
    
  @Autowired
  UserCollectionRepository userCollectionRepository;

  @Autowired
  UserEntityRepository userEntityRepository;
  
  private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	  
	  boolean userExists = dbConnection.getDatabaseInstance().existsByEmail(null,email);
	  
	  if(userExists)
	  {
		  if(dbConnection.getDatabaseInstance() instanceof TenantAspectMongo)
		  {
			  logger.info("returning mongo bean");
			  UserCollection userCollection = userCollectionRepository.findByEmailId(email);
	          return MyUserDetails.build(userCollection);
		  }
		  else if(dbConnection.getDatabaseInstance() instanceof TenantAspectSql)
		  {
			  logger.info("returning mysql bean");
			  UserEntity userEntity = userEntityRepository.findByEmail(email);
	          return MyUserDetails.build(userEntity);			  
		  }
	  }
	  
	  logger.info("returning null");
	  return null; 
  }

}
