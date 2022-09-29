package com.datafoundry.loginUserService.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name= "user_role")
public class UserRole implements Serializable{
	
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
 
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	@Id
	@Column(name="user_id")
	private String emailID;
	
	@Id
	@Column(name="user_tenant_id")
	private String tenantID;
	
	@Id
	@Column(name="role_id")
	private String roleID;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public UserRole(String emailID, String tenantID, String roleID)
	{
		this.emailID = emailID;
		this.roleID = roleID;
		this.tenantID = tenantID;
	}
	
	public UserRole()
	{
		
	}

}
