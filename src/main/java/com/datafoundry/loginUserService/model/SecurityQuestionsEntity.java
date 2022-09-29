package com.datafoundry.loginUserService.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name= "security_questions")
public class SecurityQuestionsEntity {
	
	@Id
	@NotBlank
    @Column(name="id")
    private String id;	
	
	@NotBlank
    @Column(name="emailid")
    private String email;	
    
    @NotBlank    
    @Column(name="tenantid")
    private String tenantID;
	
    @Column(name="securityquestion1")
    private String securityQuestion1;
    
    @Column(name="securityanswer1")
    private String securityAnswer1;
    
    @Column(name="securityquestion2")
    private String securityQuestion2;

    @Column(name="securityanswer2")
    private String securityAnswer2;

    @Column(name="securityquestion3")
    private String securityQuestion3;    

    @Column(name="securityanswer3")
    private String securityAnswer3;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
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

	public SecurityQuestionsEntity(@NotBlank String email, @NotBlank String tenantID,
			String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2,
			String securityQuestion3, String securityAnswer3) {
		super();
		
		String uuid = UUID.randomUUID().toString();
		this.id = uuid;
		this.email = email;
		this.tenantID = tenantID;
		this.securityQuestion1 = securityQuestion1;
		this.securityAnswer1 = securityAnswer1;
		this.securityQuestion2 = securityQuestion2;
		this.securityAnswer2 = securityAnswer2;
		this.securityQuestion3 = securityQuestion3;
		this.securityAnswer3 = securityAnswer3;
	}

	public SecurityQuestionsEntity()
	{
		String uuid = UUID.randomUUID().toString();
		this.id = uuid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, securityAnswer1, securityAnswer2, securityAnswer3, securityQuestion1,
				securityQuestion2, securityQuestion3, tenantID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityQuestionsEntity other = (SecurityQuestionsEntity) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(securityAnswer1, other.securityAnswer1)
				&& Objects.equals(securityAnswer2, other.securityAnswer2)
				&& Objects.equals(securityAnswer3, other.securityAnswer3)
				&& Objects.equals(securityQuestion1, other.securityQuestion1)
				&& Objects.equals(securityQuestion2, other.securityQuestion2)
				&& Objects.equals(securityQuestion3, other.securityQuestion3)
				&& Objects.equals(tenantID, other.tenantID);
	}

	@Override
	public String toString() {
		return "SecurityQuestions [id=" + id + ", email=" + email + ", tenantID=" + tenantID + ", securityQuestion1="
				+ securityQuestion1 + ", securityAnswer1=" + securityAnswer1 + ", securityQuestion2="
				+ securityQuestion2 + ", securityAnswer2=" + securityAnswer2 + ", securityQuestion3="
				+ securityQuestion3 + ", securityAnswer3=" + securityAnswer3 + "]";
	}
	
}
