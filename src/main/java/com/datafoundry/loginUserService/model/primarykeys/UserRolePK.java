package com.datafoundry.loginUserService.model.primarykeys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserRolePK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="roleid")
	private long roleID;
  
  @Id
  @Column(name="emailid")
	private String emailID;
  
  @Id
  @Column(name="tenantid")
	private String tenantID;
  
  public String getEmailID() {
	return emailID;
}

public void setEmailID(String emailID) {
	this.emailID = emailID;
}


  

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}



    public UserRolePK(long roleID, String emailID, String tenantID) {
        this.roleID = roleID;
        this.tenantID = tenantID;
        this.emailID = emailID;
    }

    public UserRolePK() {
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        UserRolePK pk = (UserRolePK) o;
        return (this.roleID == pk.getRoleID()) &&
                this.tenantID.equals(pk.getTenantID());
    }

    @Override
    public int hashCode() {
        return emailID.hashCode();
    }
}