package com.moyur.jwt;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails  implements UserDetails {
	
	private static final long serialVersionUID = 1L;

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {

        this.userEntity = userEntity;
    }
    
    public UserEntity getUserEntity() {
        return this.userEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

            	return userEntity.getRole();
            }
        });

        return collection;
    }
    
    @Override
    public String getUsername() {

        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {

        return userEntity.getPassword();
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