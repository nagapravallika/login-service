package com.datafoundry.loginUserService.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {

  	private static final long serialVersionUID = 1L;
  	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
    @Column(name="roleid")
    private String roleID;

    @Column(name="tenantid")
    private String tenantID;
    
    @Column(name="rolename")
    private String roleName;
    
    @Column(name="roledisplayname")
    private String roleDisplayName;   
    
    @Column(name="status")
    private String status;   
    
    @Column(name="createdBy")
    private String createdBy;
    
    @Column(name="createdTime")
    private Date createdTime;
    
    @Column(name="updatedBy")
    private String updatedBy;
    
    @Column(name="updatedTime")
    private Date updatedTime;

	public String getStatus() {
		return status;
	}
	
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_permissions", 
    joinColumns =  { @JoinColumn(name = "roleid", referencedColumnName = "roleid") },
    inverseJoinColumns = { @JoinColumn(name = "permissionid", referencedColumnName = "id") })
    private Set<PermissionEntity> permissions = new HashSet<PermissionEntity>();

	public Set<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionEntity> permissions) {
		this.permissions = permissions;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RoleEntity() {

    }
    

  	public RoleEntity(String tenantID, String roleName, String roleDisplayName, String status,
			String createdBy, Date createdTime, String updatedBy, Date updatedTime) {
		super();
		UUID uuid = UUID.randomUUID();
		this.roleID = uuid.toString();
		this.tenantID = tenantID;
		this.roleName = roleName;
		this.roleDisplayName = roleDisplayName;
		this.status = status;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
	}
    public String getRoleID() 
    {
    	return roleID;
    }

    public void setRoleID(String roleID) 
    {
    	this.roleID = roleID;
    }
   
    public String getTenantID() {
  		return this.tenantID;
  	} 	

  	public void setTenantID(String tenantID) {
  		this.tenantID  = tenantID;
  	}
  	
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
  	



	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdTime, permissions, roleDisplayName, roleID, roleName, status, tenantID,
				updatedBy, updatedTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntity other = (RoleEntity) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdTime, other.createdTime)
				&& Objects.equals(permissions, other.permissions)
				&& Objects.equals(roleDisplayName, other.roleDisplayName) && Objects.equals(roleID, other.roleID)
				&& Objects.equals(roleName, other.roleName) && Objects.equals(status, other.status)
				&& Objects.equals(tenantID, other.tenantID) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedTime, other.updatedTime);
	}

	@Override
	public String toString() {
		return "RoleEntity [roleID=" + roleID + ", tenantID=" + tenantID + ", roleName=" + roleName
				+ ", roleDisplayName=" + roleDisplayName + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdTime=" + createdTime + ", updatedBy=" + updatedBy + ", updatedTime=" + updatedTime
				+ ", permissions=" + permissions + "]";
	}
	
	public String toAuditString() {
		return "roleID=" + roleID + "~ tenantID=" + tenantID + "~ roleName=" + roleName
				+ "~ roleDisplayName=" + roleDisplayName + "~ status=" + status + "~ createdBy=" + createdBy
				+ "~ createdTime=" + createdTime + "~ updatedBy=" + updatedBy + "~ updatedTime=" + updatedTime
				+ "~ permissions=" + permissions;
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
  	
	
}
