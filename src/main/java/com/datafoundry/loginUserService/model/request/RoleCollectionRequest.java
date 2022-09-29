package com.datafoundry.loginUserService.model.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.datafoundry.loginUserService.model.Permission;

import io.swagger.annotations.ApiModelProperty;

public class RoleCollectionRequest {

	private String clientId;
	private Permission[] permissions;
	
	@NotBlank
	@ApiModelProperty(notes = "Role Name", example = "dbadmin", required = true)
	protected String roleName;
	  
	@NotBlank
	@ApiModelProperty(notes = "Role Display Name", example = "Database Administrator", required = true)
	protected String roleDisplayName;
	  
	protected String status;
	protected String createdBy;
	protected String updatedBy;
	protected Date createdTime;
	protected Date updatedTime;
		
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDisplayName() {
		return roleDisplayName;
	}
	public void setRoleDisplayName(String roleDisplayName) {
		this.roleDisplayName = roleDisplayName;
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
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Permission[] getPermissions() {
		return permissions;
	}
	public void setPermissions(Permission[] permissions) {
		this.permissions = permissions;
	}
	
	
	
}
