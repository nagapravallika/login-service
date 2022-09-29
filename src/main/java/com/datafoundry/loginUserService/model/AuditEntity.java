package com.datafoundry.loginUserService.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name= "audit")
public class AuditEntity {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
	private long id;
	
	@Column(name="userid")
	private String emailID;
	
	@Column(name="roleid")
	private long roleID;
	
	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	@Column(name="tenantid")
	private String tenantID;
	
	@Column(name="user_role")
	private String modifiedUserRole;
	
	@Column(name="modified_by")
	private String modifiedBy;
	
	@Column(name="modified_date_time")
	private Date modifiedDateTime;
	
	@Column(name="operation")
	private String operation;
	
	@Column(name="service_name")
	private String serviceName;
	
	@Lob
	@Column(name="updated_data")
	private String updatedData;
	
	@Lob
	@Column(name="created_data")
	private String createdData;
	
	@Lob
	@Column(name="dropped_data")
	private String droppedData;

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

	public String getModifiedUserRole() {
		return modifiedUserRole;
	}

	public void setModifiedUserRole(String modifiedUserRole) {
		this.modifiedUserRole = modifiedUserRole;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUpdatedData() {
		return updatedData;
	}

	public void setUpdatedData(String updatedData) {
		this.updatedData = updatedData;
	}

	public String getCreatedData() {
		return createdData;
	}

	public void setCreatedData(String createdData) {
		this.createdData = createdData;
	}

	public String getDroppedData() {
		return droppedData;
	}

	public void setDroppedData(String droppedData) {
		this.droppedData = droppedData;
	}

	@Override
	public String toString() {
		return "AuditEntity [id=" + id + ", emailID=" + emailID + ", tenantID=" + tenantID + ", modifiedUserRole="
				+ modifiedUserRole + ", modifiedBy=" + modifiedBy + ", modifiedDateTime=" + modifiedDateTime
				+ ", operation=" + operation + ", serviceName=" + serviceName + ", updatedData=" + updatedData
				+ ", createdData=" + createdData + ", droppedData=" + droppedData + "]";
	}
	

}
