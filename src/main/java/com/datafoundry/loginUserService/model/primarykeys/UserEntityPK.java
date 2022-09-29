package com.datafoundry.loginUserService.model.primarykeys;

import java.io.Serializable;

public class UserEntityPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String tenantID;
	
    

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

    public UserEntityPK(String email, String tenantID) {
        this.email = email;
        this.tenantID = tenantID;
    }

    public UserEntityPK() {
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        UserEntityPK pk = (UserEntityPK) o;
        return this.email.equals(pk.getEmail()) &&
                this.tenantID.equals(pk.getTenantID());
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}