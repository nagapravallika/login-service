package com.datafoundry.loginUserService.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Login", timeToLive = 900)
public class GenerateToken implements Serializable {

	private static final long serialVersionUID = 7315271718965141412L;
	
	@Id
	private String jwt;
	
	private String username;
	


	public GenerateToken(String jwt, String username) {
		this.jwt = jwt;
		this.username = username;
	}

	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
