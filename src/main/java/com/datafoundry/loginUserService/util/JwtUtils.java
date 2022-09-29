package com.datafoundry.loginUserService.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

	@Value("${app.secret}")
	private String secret;
	
	@Value("${app.tokenExpireTime}")
    private int tokenExpireTime;

	//Generate Token
	public String generateToken(String Subject) {
		return Jwts.builder()
					.setSubject(Subject)
					.setIssuer("DataFoundry")
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date((new Date()).getTime() + tokenExpireTime))
					.signWith(SignatureAlgorithm.HS512, secret.getBytes())
					.compact();
	}
	
	//Read claims
	public Claims getClaims(String token) {
		return Jwts.parser()
				   .setSigningKey(secret.getBytes())
				   .parseClaimsJws(token)
				   .getBody();
	}
	
	//read expiration date 
    public Date getExpDate(String token) {
		return getClaims(token).getExpiration();
    }
	
    //read username/subject
    public String getUsername(String token) {
		return getClaims(token).getSubject();
    }

	//valid exp date
    public Boolean isTokenExp(String token) {
    	Date expDate =getExpDate(token);
		return expDate.before(new Date(System.currentTimeMillis()));
    }
    
    //validate username in token and databse, expdate
    public boolean validateToken(String token,String username) {
    	String tokenUsername = getUsername(token);
    	return (username.equals(tokenUsername) && !isTokenExp(token));
    }

}

    
	


    
   
   
    


