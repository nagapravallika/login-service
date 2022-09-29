package com.datafoundry.loginUserService.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role")
public class RoleCollection {

  @Id
  private String _id;
  
  private String roleId;
  private String roleName;
  private String clientId;
  private String roleDisplayName;
  private Permission[] permissions;
  private String status;
  private String createdBy;
  private Date createdTime;
  private String updatedBy;
  private Date updatedTime;

  
  //private ERole roleName;

  public String get_id() {
	return _id;
}

public void set_id(String _id) {
	this._id = _id;
}

public String getRoleId() {
	return roleId;
}

public void setRoleId(String roleId) {
	this.roleId = roleId;
}

public String getRoleName() {
	return roleName;
}

public void setRoleName(String roleName) {
	this.roleName = roleName;
}

public String getClientId() {
	return clientId;
}

public void setClientId(String clientId) {
	this.clientId = clientId;
}

public String getRoleDisplayName() {
	return roleDisplayName;
}

public void setRoleDisplayName(String roleDisplayName) {
	this.roleDisplayName = roleDisplayName;
}

public Permission[] getPermissions() {
	return permissions;
}

public void setPermissions(Permission[] permissions) {
	this.permissions = permissions;
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

public RoleCollection() {

  }

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(permissions);
	result = prime * result + Objects.hash(_id, clientId, createdBy, createdTime, roleDisplayName, roleId, roleName,
			status, updatedBy, updatedTime);
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
	RoleCollection other = (RoleCollection) obj;
	return Objects.equals(_id, other._id) && Objects.equals(clientId, other.clientId)
			&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdTime, other.createdTime)
			&& Arrays.equals(permissions, other.permissions) && Objects.equals(roleDisplayName, other.roleDisplayName)
			&& Objects.equals(roleId, other.roleId) && Objects.equals(roleName, other.roleName)
			&& Objects.equals(status, other.status) && Objects.equals(updatedBy, other.updatedBy)
			&& Objects.equals(updatedTime, other.updatedTime);
}

@Override
public String toString() {
	return "RoleCollection [_id=" + _id + ", roleId=" + roleId + ", roleName=" + roleName + ", clientId=" + clientId
			+ ", roleDisplayName=" + roleDisplayName + ", permissions=" + Arrays.toString(permissions) + ", status="
			+ status + ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", updatedBy=" + updatedBy
			+ ", updatedTime=" + updatedTime + "]";
}

public RoleCollection(String roleId, String roleName, String clientId, String roleDisplayName,
		Permission[] permissions, String status, String createdBy, Date createdTime, String updatedBy,
		Date updatedTime) {
	super();
	this.roleId = roleId;
	this.roleName = roleName;
	this.clientId = clientId;
	this.roleDisplayName = roleDisplayName;
	this.permissions = permissions;
	this.status = status;
	this.createdBy = createdBy;
	this.createdTime = createdTime;
	this.updatedBy = updatedBy;
	this.updatedTime = updatedTime;
}
	
}
