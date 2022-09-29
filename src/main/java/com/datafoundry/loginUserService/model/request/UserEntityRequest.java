package com.datafoundry.loginUserService.model.request;

import java.sql.Timestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;

public class UserEntityRequest {

  public static final String PWD_MSG = "password contains atleast minimum 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 digit and 1 special character (`~#?!@$%^&*-+=_:;)";
	
//  @NotBlank(message = "username cannot be null")
//  @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
//  @ApiModelProperty(notes = "Username", example = "xxxxx", required = true)
//  private String username;

  @NotBlank(message = "Email id cannot be null")
  @Email(message = "Invalid email. Try again.")
  @ApiModelProperty(notes = "Email", example = "xxx@datafoundry.ai", required = true)
  private String email;

  @NotBlank(message = "password cannot be null")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = PWD_MSG)
  @ApiModelProperty(notes = "Password", example = "Xyz123@r", required = true)
  private String password;

//  @ApiModelProperty(dataType = "Set", example = "'[''{''Role01''}','{''Role02''}',' {''Role03''}'']" ,allowEmptyValue = false)
    @ApiModelProperty(hidden = true)
    private String[] roleIDs;
  
//  @ApiModelProperty(dataType = "Set", example = "'[''{''Role01''}','{''Role02''}',' {''Role03''}'']" ,allowEmptyValue = false)
    @ApiModelProperty(hidden = true)
    private String[] roleNames;


  @NotBlank
  @ApiModelProperty(notes = "Tenant ID", example = "ABC1234", required = true)
  private String tenantID;
	
  @NotBlank
  @ApiModelProperty(notes = "First Name", example = "John", required = true)
  private String firstName;

  @NotBlank
  @ApiModelProperty(notes = "Last Name", example = "Doe", required = true)
  private String lastName;

  @Email
  @ApiModelProperty(notes = "Manager's Email ID", example = "john.doe@datafoundry.ai", required = false)
  private String managerEmailID;

  @ApiModelProperty(notes = "Security Question 1", example = "Your favorite place", required = false)
  private String securityQuestion1;

  @ApiModelProperty(notes = "Security Answer 1", example = "My hometown", required = false)
  private String securityAnswer1;

  @ApiModelProperty(notes = "Security Question 2", example = "Your pet name", required = false)
  private String securityQuestion2;

  @ApiModelProperty(notes = "Security Answer 2", example = "Late bloomer", required = false)
  private String securityAnswer2;

  @ApiModelProperty(notes = "Security Question 3", example = "Your birth place", required = false)	
  private String securityQuestion3;

  @ApiModelProperty(notes = "Security Answer 3", example = "Hyderabad", required = false)
  private String securityAnswer3;
	  
//  @ApiModelProperty(notes = "User's Login Status", example = "Active / Inactive / Locked", required = false)
    @ApiModelProperty(hidden = true)
    private String status;
	
//  @ApiModelProperty(notes = "Number of unsuccessful login attempts. Max allowed is 3", example = "0", required = false)
    @ApiModelProperty(hidden = true)
    private int unsuccessfulAttempts;
	  
//  @ApiModelProperty(notes = "User's last login time", example = "23rd August 2022 18:03:25 IST", required = false)
    @ApiModelProperty(hidden = true)
    private Timestamp lastLoginTime;
  

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



public String getTenantID() {
	return tenantID;
}

public void setTenantID(String tenantID) {
	this.tenantID = tenantID;
}

public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

public String getManagerEmailID() {
	return managerEmailID;
}

public void setManagerEmailID(String managerEmailID) {
	this.managerEmailID = managerEmailID;
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

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public int getUnsuccessfulAttempts() {
	return unsuccessfulAttempts;
}

public void setUnsuccessfulAttempts(int unsuccessfulAttempts) {
	this.unsuccessfulAttempts = unsuccessfulAttempts;
}

public Timestamp getLastLoginTime() {
	return lastLoginTime;
}

public void setLastLoginTime(Timestamp lastLoginTime) {
	this.lastLoginTime = lastLoginTime;
}

public String[] getRoleNames() {
	return roleNames;
}

public void setRoleNames(String[] roleNames) {
	this.roleNames = roleNames;
}

public UserEntityRequest(
		@NotBlank(message = "Email id cannot be null") @Email(message = "Invalid email. Try again.") String email,
		@NotBlank(message = "password cannot be null") @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "password contains atleast minimum 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 digit and 1 special character (`~#?!@$%^&*-+=_:;)") String password,
		String[] roleNames, @NotBlank String tenantID, @NotBlank String firstName, @NotBlank String lastName,
		@Email String managerEmailID, String securityQuestion1, String securityAnswer1, String securityQuestion2,
		String securityAnswer2, String securityQuestion3, String securityAnswer3, @NotBlank String status,
		int unsuccessfulAttempts, Timestamp lastLoginTime) {
	super();
	this.email = email;
	this.password = password;
	this.roleNames = roleNames;
	this.tenantID = tenantID;
	this.firstName = firstName;
	this.lastName = lastName;
	this.managerEmailID = managerEmailID;
	this.securityQuestion1 = securityQuestion1;
	this.securityAnswer1 = securityAnswer1;
	this.securityQuestion2 = securityQuestion2;
	this.securityAnswer2 = securityAnswer2;
	this.securityQuestion3 = securityQuestion3;
	this.securityAnswer3 = securityAnswer3;
	this.status = status;
	this.unsuccessfulAttempts = unsuccessfulAttempts;
	this.lastLoginTime = lastLoginTime;
}

public String[] getRoleIDs() {
	return roleIDs;
}

public void setRoleIDs(String[] roleIDs) {
	this.roleIDs = roleIDs;
}

public UserEntityRequest()
{
	
}
}
