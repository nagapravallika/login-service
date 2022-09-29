package com.datafoundry.loginUserService.model.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.datafoundry.loginUserService.model.PermissionEntity;

import io.swagger.annotations.ApiModelProperty;

public class RoleEntityRequest {

  @NotBlank
  @ApiModelProperty(notes = "Tenant ID", example = "Tenant1234", required = true)
  private String tenantID;
  
  @NotBlank
  @ApiModelProperty(notes = "Role Name", example = "dbadmin", required = true)
  private String roleName;
  
  @NotBlank
  @ApiModelProperty(notes = "Role Display Name", example = "Database Administrator", required = true)
  private String roleDisplayName;
  
  private String status;
  private String createdBy;
  private String updatedBy;
  private Date createdTime;
  private Date updatedTime;
  private PermissionEntity[] permissions;
		
	  
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
  
  	public String getTenantID() {
		return tenantID;
	}
	
	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}
	
	public PermissionEntity[] getPermissions() {
		return permissions;
	}
	
	public void setPermissions(PermissionEntity[] permissions) {
		this.permissions = permissions;
	}
	
	public RoleEntityRequest(@NotBlank String roleDisplayName, String status, String createdBy, String updatedBy,
			PermissionEntity[] permissions) {
		super();
		this.roleDisplayName = roleDisplayName;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.permissions = permissions;
	}
	
	public RoleEntityRequest(@NotBlank String tenantID, @NotBlank String roleName, @NotBlank String roleDisplayName,
			String status, String createdBy, String updatedBy, Date createdTime, Date updatedTime,
			PermissionEntity[] permissions) {
		super();
		this.tenantID = tenantID;
		this.roleName = roleName;
		this.roleDisplayName = roleDisplayName;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.permissions = permissions;
	}  
	
	public RoleEntityRequest()
	{
		
	};
	
	

}
