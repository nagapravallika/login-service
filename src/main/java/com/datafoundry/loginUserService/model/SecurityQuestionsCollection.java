package com.datafoundry.loginUserService.model;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "security_questions")
public class SecurityQuestionsCollection {

	private String _id;
	
    private String emailId;	

    private String clientId;

    private String userId;
    
    private String securityQuestion1;
    
    private String securityAnswer1;
    
    private String securityQuestion2;

    private String securityAnswer2;

    private String securityQuestion3;    

    private String securityAnswer3;

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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public SecurityQuestionsCollection(String emailId, String clientId, String userId, String securityQuestion1,
			String securityAnswer1, String securityQuestion2, String securityAnswer2, String securityQuestion3,
			String securityAnswer3) {
		super();
		this.emailId = emailId;
		this.clientId = clientId;
		this.userId = userId;
		this.securityQuestion1 = securityQuestion1;
		this.securityAnswer1 = securityAnswer1;
		this.securityQuestion2 = securityQuestion2;
		this.securityAnswer2 = securityAnswer2;
		this.securityQuestion3 = securityQuestion3;
		this.securityAnswer3 = securityAnswer3;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id, clientId, emailId, securityAnswer1, securityAnswer2, securityAnswer3,
				securityQuestion1, securityQuestion2, securityQuestion3, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityQuestionsCollection other = (SecurityQuestionsCollection) obj;
		return Objects.equals(_id, other._id) && Objects.equals(clientId, other.clientId)
				&& Objects.equals(emailId, other.emailId) && Objects.equals(securityAnswer1, other.securityAnswer1)
				&& Objects.equals(securityAnswer2, other.securityAnswer2)
				&& Objects.equals(securityAnswer3, other.securityAnswer3)
				&& Objects.equals(securityQuestion1, other.securityQuestion1)
				&& Objects.equals(securityQuestion2, other.securityQuestion2)
				&& Objects.equals(securityQuestion3, other.securityQuestion3) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "SecurityQuestionsCollection [_id=" + _id + ", emailId=" + emailId + ", clientId=" + clientId
				+ ", userId=" + userId + ", securityQuestion1=" + securityQuestion1 + ", securityAnswer1="
				+ securityAnswer1 + ", securityQuestion2=" + securityQuestion2 + ", securityAnswer2=" + securityAnswer2
				+ ", securityQuestion3=" + securityQuestion3 + ", securityAnswer3=" + securityAnswer3 + "]";
	}
    
}
