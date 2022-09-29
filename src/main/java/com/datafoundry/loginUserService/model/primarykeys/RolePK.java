package com.datafoundry.loginUserService.model.primarykeys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class RolePK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="roleid")
	private long roleID;
  
  @Column(name="tenantid")
	private String tenantID;
  

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



    public RolePK(long roleID, String tenantID) {
        this.roleID = roleID;
        this.tenantID = tenantID;
    }

    public RolePK() {
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        RolePK pk = (RolePK) o;
        return (this.roleID == pk.getRoleID()) &&
                this.tenantID.equals(pk.getTenantID());
    }

    @Override
    public int hashCode() {
        return tenantID.hashCode();
    }
}