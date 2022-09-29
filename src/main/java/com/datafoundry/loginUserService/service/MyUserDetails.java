package com.datafoundry.loginUserService.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.datafoundry.loginUserService.model.UserCollection;
import com.datafoundry.loginUserService.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MyUserDetails implements UserDetails {

  /**
	 * 
	 */
	private static final long serialVersionUID = -7525755728492094200L;

	private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public MyUserDetails(String email, String password,
                       Collection<? extends GrantedAuthority> authorities) {
      this.email = email;
      this.password = password;
      this.authorities = authorities;
  }

  public static MyUserDetails build(UserCollection userCollection) {

	  List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	  authorities.add(new SimpleGrantedAuthority(userCollection.getRoleName()));
	  return new MyUserDetails(userCollection.getEmailId(), userCollection.getPassword(), authorities);
  }

  public static MyUserDetails build(UserEntity userEntity) {
    List<GrantedAuthority> authorities = userEntity.getUserRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
    		//.map(role -> new SimpleGrantedAuthority(""+role))
            .collect(Collectors.toList());
    return new MyUserDetails(
            userEntity.getEmail(),
            userEntity.getPassword(),
            authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
