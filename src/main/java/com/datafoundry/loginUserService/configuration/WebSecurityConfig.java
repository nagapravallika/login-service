package com.datafoundry.loginUserService.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.datafoundry.loginUserService.util.AccessDeniedExceptionHandler;
import com.datafoundry.loginUserService.util.InvalidUserAuthEntryPoint;
import com.datafoundry.loginUserService.util.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	InvalidUserAuthEntryPoint authenticationEntryPoint;

	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new AccessDeniedExceptionHandler();
	}

	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.cors().disable()
				.authorizeRequests()
				.antMatchers("/health", "/v2/api-docs/**", "/swagger-resources", "/webjars/**", 
						"/swagger-resources/configuration/ui", "/swagger-ui.html", 
						"/swagger-resources/configuration/security", "/checkSessionExists","/test/**").permitAll()
				
				.antMatchers(HttpMethod.POST ,"/authservice/v1/users/","/authservice/v1/","/authservice/v1/users/{userID}").permitAll() // register user, login, validate security questions
				.antMatchers(HttpMethod.POST ,"/authservice/v1/roles/").hasAuthority("superAdmin")  //create role
				
				
				.antMatchers(HttpMethod.PUT ,"/authservice/v1/users/{emailID}").permitAll() //update one user profile (first name, last name etc)
				
				.antMatchers(HttpMethod.PATCH ,"/authservice/v1/users/{userID}").permitAll() //update one user (change password, deactivate)
				.antMatchers(HttpMethod.PATCH ,"/authservice/v1/users/{userID}","/authservice/v1/users/{emailID}/roles/{roleNames}").hasAuthority("superAdmin")  //update user, assign roles to user
				
				.antMatchers(HttpMethod.GET ,"/authservice/v1/users/","/authservice/v1/users/{userID}","/authservice/v1/roles/","/authservice/v1/roles/{roleName}").hasAuthority("superAdmin") // get all users, get one user, get all roles and get one role.
				
				.antMatchers(HttpMethod.DELETE ,"/authservice/v1/users/{userID}").permitAll() //invalidate user session
				.antMatchers(HttpMethod.DELETE ,"/authservice/v1/users/{emailID}/roles/{roleNames}").hasAuthority("superAdmin")		//revoke roles from user					
				.antMatchers(HttpMethod.DELETE ,"/authservice/v1/roles/{roleName}").hasAuthority("superAdmin") // delete role
				
				.anyRequest().authenticated()
				.and()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
	}

}
