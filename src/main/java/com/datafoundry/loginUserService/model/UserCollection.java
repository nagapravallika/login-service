package com.datafoundry.loginUserService.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class UserCollection
{


	@Id
  private String _id;
  private String emailId;
  private String userId;
  private String roleId;
  private String clientId;
  private String password;
  private String[] subdomain;
  private String status;
  private String createdBy;
  private Date createdTime;
  private String updatedBy;
  private Date updatedTime;
  private Date lastLoginTime;
  private String name;
  private String manager;
  private String roleName;
  private String updateBy;
  private String department;
  private String designation;
  private String mobileNo;
  private String userPicURL;
  private int unsuccessfulAttempts;
  private String locale;
  private String firstName;
  private String lastName;

	private boolean profileLocked;
  
	public boolean isProfileLocked() {
		return profileLocked;
	}

	public void setProfileLocked(boolean profileLocked) {
		this.profileLocked = profileLocked;
	}

	
	
	@Override
	public String toString() {
		return "UserCollection [_id=" + _id + ", emailId=" + emailId + ", userId=" + userId + ", roleId=" + roleId
				+ ", clientId=" + clientId + ", password=" + password + ", subdomain=" + Arrays.toString(subdomain)
				+ ", status=" + status + ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", updatedBy="
				+ updatedBy + ", updatedTime=" + updatedTime + ", lastLoginTime=" + lastLoginTime + ", name=" + name
				+ ", manager=" + manager + ", roleName=" + roleName + ", updateBy=" + updateBy + ", department="
				+ department + ", designation=" + designation + ", mobileNo=" + mobileNo + ", userPicURL=" + userPicURL
				+ ", unsuccessfulAttempts=" + unsuccessfulAttempts + ", locale=" + locale + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", profileLocked=" + profileLocked + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(subdomain);
		result = prime * result + Objects.hash(_id, clientId, createdBy, createdTime, department, designation, emailId,
				firstName, lastLoginTime, lastName, locale, manager, mobileNo, name, password, profileLocked, roleId,
				roleName, status, unsuccessfulAttempts, updateBy, updatedBy, updatedTime, userId, userPicURL);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCollection other = (UserCollection) obj;
		return Objects.equals(_id, other._id) && Objects.equals(clientId, other.clientId)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdTime, other.createdTime)
				&& Objects.equals(department, other.department) && Objects.equals(designation, other.designation)
				&& Objects.equals(emailId, other.emailId) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastLoginTime, other.lastLoginTime) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(locale, other.locale) && Objects.equals(manager, other.manager)
				&& Objects.equals(mobileNo, other.mobileNo) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && profileLocked == other.profileLocked
				&& Objects.equals(roleId, other.roleId) && Objects.equals(roleName, other.roleName)
				&& Objects.equals(status, other.status) && Arrays.equals(subdomain, other.subdomain)
				&& unsuccessfulAttempts == other.unsuccessfulAttempts && Objects.equals(updateBy, other.updateBy)
				&& Objects.equals(updatedBy, other.updatedBy) && Objects.equals(updatedTime, other.updatedTime)
				&& Objects.equals(userId, other.userId) && Objects.equals(userPicURL, other.userPicURL);
	}

	public UserCollection()
	{
		
	}
	
	public UserCollection(String emailId, String userId, String roleId, String clientId, String password,
			String[] subdomain, String status, String createdBy, Date createdTime, String updatedBy, Date updatedTime,
			Date lastLoginTime, String name, String manager, String roleName, String updateBy, String department,
			String designation, String mobileNo, String userPicURL, int unsuccessfulAttempts, String locale,
			String firstName, String lastName) {
		super();
		this.emailId = emailId;
		this.userId = userId;
		this.roleId = roleId;
		this.clientId = clientId;
		this.password = password;
		this.subdomain = subdomain;
		this.status = status;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.lastLoginTime = lastLoginTime;
		this.name = name;
		this.manager = manager;
		this.roleName = roleName;
		this.updateBy = updateBy;
		this.department = department;
		this.designation = designation;
		this.mobileNo = mobileNo;
		this.userPicURL = userPicURL;
		this.unsuccessfulAttempts = unsuccessfulAttempts;
		this.locale = locale;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String[] subdomain) {
		this.subdomain = subdomain;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUserPicURL() {
		return userPicURL;
	}
	public void setUserPicURL(String userPicURL) {
		this.userPicURL = userPicURL;
	}
	public int getUnsuccessfulAttempts() {
		return unsuccessfulAttempts;
	}
	public void setUnsuccessfulAttempts(int unsuccessfulAttempts) {
		this.unsuccessfulAttempts = unsuccessfulAttempts;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
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
    
}
