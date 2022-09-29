package com.datafoundry.loginUserService.model.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {

  @NotBlank(message = "Email id cannot be null")
  @Email(message = "Invalid email. Try again.")
  @ApiModelProperty(notes = "Email", example = "xxx@datafoundry.ai", required = true)
  private String email;

  @NotBlank(message = "password cannot be null")
  @ApiModelProperty(notes = "Password", example = "Xyz123@r", required = true)
  private String password;
  
  @NotBlank(message = "Tenant ID cannot be null")
  @ApiModelProperty(notes = "Tenant ID", example = "tenant001", required = true)
  private String tenantID;

  public String getTenantID() {
	return tenantID;
}

public void setTenantID(String tenantID) {
	this.tenantID = tenantID;
}

public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LoginRequest(
		@NotBlank(message = "Email id cannot be null") @Email(message = "Invalid email. Try again.") String email,
		@NotBlank(message = "password cannot be null") String password,
		@NotBlank(message = "Tenant ID cannot be null") String tenantID) {
	super();
	this.email = email;
	this.password = password;
	this.tenantID = tenantID;
}
  
  

}
