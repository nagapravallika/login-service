package com.datafoundry.loginUserService.util;

import com.datafoundry.loginUserService.LoginUserServiceApplication;
import com.datafoundry.loginUserService.service.GenerateTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	GenerateTokenService generateTokenService;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceApplication.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			 						throws ServletException, IOException {
		
		//read token from authorization header
		 String token = request.getHeader("Authorization");
		 logger.info("inside the filter, got the token ->"+token);
		 if ((token != null)
				 && (generateTokenService.isExistsByToken(token))) {
			 //do validation
			 String email = jwtUtils.getUsername(token);
			 logger.info("email obtained from the token is "+email);
			 if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				 UserDetails usr = this.userDetailsService.loadUserByUsername(email);

				 //do validation
				 boolean isValid = jwtUtils.validateToken(token,usr.getUsername());
				 logger.info("is the token valid ? "+isValid);
				 if (isValid) {
					 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,usr.getPassword(), usr.getAuthorities());
					 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					 //final object store in security context with userdetails with username and password
					 SecurityContextHolder.getContext().setAuthentication(authToken);
				 }
			 }
		 }
		 filterChain.doFilter(request, response);
	}

}
