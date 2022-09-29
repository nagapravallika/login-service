package com.datafoundry.loginUserService.model.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SecurityQuestionsRequest {

  
  @NotBlank(message = "Email id cannot be null")
  @Email(message = "Invalid email. Try again.")
  @ApiModelProperty(notes = "Email", example = "xxx@datafoundry.ai", required = true)
  private String emailId;	

  @NotBlank(message = "Client id cannot be null")
  @ApiModelProperty(notes = "clientId", required = true)
  private String tenantID;
  
  @NotBlank(message = "security Question 1 cannot be null")
  @ApiModelProperty(notes = "securityQuestion1", required = true)
  private String securityQuestion1;
  
  @NotBlank(message = "security Answer 1 cannot be null")
  @ApiModelProperty(notes = "securityAnswer1", required = true)
  private String securityAnswer1;
  
  @NotBlank(message = "Security Question 2 cannot be null")
  @ApiModelProperty(notes = "securityQuestion2", required = true)
  private String securityQuestion2;

  @NotBlank(message = "Security Answer 2 cannot be null")
  @ApiModelProperty(notes = "securityAnswer2", required = true)
  private String securityAnswer2;

  @NotBlank(message = "Security Question 3 cannot be null")
  @ApiModelProperty(notes = "securityQuestion3", required = true)
  private String securityQuestion3;    

  @NotBlank(message = "Security Answer 3 cannot be null")
  @ApiModelProperty(notes = "securityAnswer3", required = true)
  private String securityAnswer3;

	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getClientId() {
		return tenantID;
	}
	
	public void setClientId(String clientId) {
		this.tenantID = clientId;
	}
	
	public String getSecurityQuestion1() {
		return securityQuestion1;
	}
	
	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}
	
	public String getSecurityAnswer1() {
		return securityAnswer1;
	}
	
	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}
	
	public String getSecurityQuestion2() {
		return securityQuestion2;
	}
	
	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}
	
	public String getSecurityAnswer2() {
		return securityAnswer2;
	}
	
	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}
	
	public String getSecurityQuestion3() {
		return securityQuestion3;
	}
	
	public void setSecurityQuestion3(String securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}
	
	public String getSecurityAnswer3() {
		return securityAnswer3;
	}
	
	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}

	public SecurityQuestionsRequest(
			@NotBlank(message = "Email id cannot be null") @Email(message = "Invalid email. Try again.") String emailId,
			@NotBlank(message = "Client id cannot be null") String tenantId,
			@NotBlank(message = "security Question 1 cannot be null") String securityQuestion1,
			@NotBlank(message = "security Answer 1 cannot be null") String securityAnswer1,
			@NotBlank(message = "Security Question 2 cannot be null") String securityQuestion2,
			@NotBlank(message = "Security Answer 2 cannot be null") String securityAnswer2,
			@NotBlank(message = "Security Question 3 cannot be null") String securityQuestion3,
			@NotBlank(message = "Security Answer 3 cannot be null") String securityAnswer3) {
		super();
		this.emailId = emailId;
		this.tenantID = tenantId;
		this.securityQuestion1 = securityQuestion1;
		this.securityAnswer1 = securityAnswer1;
		this.securityQuestion2 = securityQuestion2;
		this.securityAnswer2 = securityAnswer2;
		this.securityQuestion3 = securityQuestion3;
		this.securityAnswer3 = securityAnswer3;
	}

	
}