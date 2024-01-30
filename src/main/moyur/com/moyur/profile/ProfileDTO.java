package com.moyur.profile;

import org.springframework.web.multipart.MultipartFile;

public class ProfileDTO {

    private MultipartFile profileImageUrl;
    private String username;
    private String userType;

    public ProfileDTO(MultipartFile profileImageUrl, String username, String imageType) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.userType = imageType;
    }

	public MultipartFile getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(MultipartFile profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
    
}