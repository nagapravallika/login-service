package com.datafoundry.loginUserService.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.datafoundry.loginUserService.model.primarykeys.UserEntityPK;

@Entity
@Table(name= "users")
@IdClass(UserEntityPK.class)
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotBlank
    @Column(name="emailid")
    private String email;	
    
    @Id
    @NotBlank    
    @Column(name="tenantid")
    private String tenantID;

	@NotBlank
    @Column(name="password")
    private String password;
    
    @NotBlank
    @Column(name="firstname")
    private String firstName;
    
    @NotBlank
    @Column(name="lastname")
    private String lastName;
    
    @Column(name="manager")
    private String managerEmailID;

    @Column(name="status")
    private String status;
    
    @Column(name="unsuccessfulattempts")
    private int unsuccessfulAttempts;
    
    @Column(name="lastlogintime")
    private Date lastLoginTime;
    
    
    @Column(name="profile_locked")
    private boolean profileLocked;
    
    @Column(name="security_questions_id")
    private String securityQuestionsID;
    
    public String getSecurityQuestionsID() {
		return securityQuestionsID;
	}

	public void setSecurityQuestionsID(String securityQuestionsID) {
		this.securityQuestionsID = securityQuestionsID;
	}

	public boolean isProfileLocked() {
		return profileLocked;
	}

	public void setProfileLocked(boolean profileLocked) {
		this.profileLocked = profileLocked;
	}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", 
    joinColumns = 
      { @JoinColumn(name = "user_id", referencedColumnName = "emailid"),@JoinColumn(name = "user_tenant_id", referencedColumnName = "tenantid")  },
    inverseJoinColumns = 
      { @JoinColumn(name = "role_id", referencedColumnName = "roleid") })
    private Set<RoleEntity> userRoles = new HashSet<RoleEntity>();
    
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "security_questions_id", referencedColumnName = "id", insertable=false, updatable=false)
    private SecurityQuestionsEntity secQuestions = new SecurityQuestionsEntity();
  
       
    public SecurityQuestionsEntity getSecQuestions() {
		return secQuestions;
	}

	public void setSecQuestions(SecurityQuestionsEntity secQuestions) {
		this.secQuestions = secQuestions;
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

	public String getManagerEmailID() {
		return managerEmailID;
	}

	public void setManagerEmailID(String managerEmailID) {
		this.managerEmailID = managerEmailID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUnsuccessfulAttempts() {
		return unsuccessfulAttempts;
	}

	public void setUnsuccessfulAttempts(int unsuccessfulAttempts) {
		this.unsuccessfulAttempts = unsuccessfulAttempts;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserEntity() {
    }

    public UserEntity(String email, String password,String firstName, String lastName,String emailID,String tenantID, 
    		String managerEmailID, String status) {
        
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = emailID;
        this.tenantID = tenantID;
        this.managerEmailID = managerEmailID;
        this.status = status;
    }

	public Set<RoleEntity> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<RoleEntity> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", tenantID=" + tenantID + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", managerEmailID=" + managerEmailID + ", status=" + status
				+ ", unsuccessfulAttempts=" + unsuccessfulAttempts + ", lastLoginTime=" + lastLoginTime
				+ ", profileLocked=" + profileLocked + ", securityQuestionsID=" + securityQuestionsID + ", userRoles="
				+ userRoles + ", secQuestions=" + secQuestions + "]";
	}
	
	public String toAuditString() {
		return "email=" + email + "~ tenantID=" + tenantID + "~ password=" + password + "~ firstName="
				+ firstName + "~ lastName=" + lastName + "~ managerEmailID=" + managerEmailID + "~ status=" + status
				+ "~ unsuccessfulAttempts=" + unsuccessfulAttempts + "~ lastLoginTime=" + lastLoginTime
				+ "~ profileLocked=" + profileLocked + "~ securityQuestionsID=" + securityQuestionsID + "~ userRoles="
				+ userRoles + "~ secQuestions=" + secQuestions ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, lastLoginTime, lastName, managerEmailID, password, profileLocked,
				secQuestions, securityQuestionsID, status, tenantID, unsuccessfulAttempts, userRoles);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastLoginTime, other.lastLoginTime) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(managerEmailID, other.managerEmailID) && Objects.equals(password, other.password)
				&& profileLocked == other.profileLocked && Objects.equals(secQuestions, other.secQuestions)
				&& Objects.equals(securityQuestionsID, other.securityQuestionsID)
				&& Objects.equals(status, other.status) && Objects.equals(tenantID, other.tenantID)
				&& unsuccessfulAttempts == other.unsuccessfulAttempts && Objects.equals(userRoles, other.userRoles);
	}

}
