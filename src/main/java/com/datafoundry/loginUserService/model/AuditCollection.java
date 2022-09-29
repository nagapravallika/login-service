package com.datafoundry.loginUserService.model;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "audit")
public class AuditCollection {
	
	@Id  
	private String _id;
	
	private String emailID;
	
	private long roleID;
	
	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	private String tenantID;
	
	private String modifiedUserRole;
	
	private String modifiedBy;
	
	private Date modifiedDateTime;
	
	private String operation;
	
	private String serviceName;
	
	private String updatedData;
	
	private String createdData;
	
	private String droppedData;
	

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
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
		return "AuditCollection [id=" + _id + ", emailID=" + emailID + ", tenantID=" + tenantID + ", modifiedUserRole="
				+ modifiedUserRole + ", modifiedBy=" + modifiedBy + ", modifiedDateTime=" + modifiedDateTime
				+ ", operation=" + operation + ", serviceName=" + serviceName + ", updatedData=" + updatedData
				+ ", createdData=" + createdData + ", droppedData=" + droppedData + "]";
	}
	

}
