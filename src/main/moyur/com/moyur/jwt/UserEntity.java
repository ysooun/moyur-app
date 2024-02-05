package com.moyur.jwt;

import java.util.ArrayList;
import java.util.List;

import com.moyur.follower.FollowerEntity;
import com.moyur.profile.ProfileEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    private String email;
    private String role;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfileEntity profile;
    
    @OneToMany(mappedBy = "user")
    private List<FollowerEntity> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<FollowerEntity> followings = new ArrayList<>(); 
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public ProfileEntity getProfile() {
		return profile;
	}
	public void setProfile(ProfileEntity profile) {
		this.profile = profile;
	}
	
	public List<FollowerEntity> getFollowers() {
		return followers;
	}
	public void setFollowers(List<FollowerEntity> followers) {
		this.followers = followers;
	}
}