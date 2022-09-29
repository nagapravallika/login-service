package com.datafoundry.loginUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class LoginUserServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);
	   
	public static void main(String[] args) {
		logger.debug("************About to start service LoginUserService *******************");
		SpringApplication.run(LoginUserServiceApplication.class, args);
		logger.debug("************Successfully started LoginUserService *******************");
	}
	


}
