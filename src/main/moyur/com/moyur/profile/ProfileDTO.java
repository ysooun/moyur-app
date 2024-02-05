package com.moyur.profile;

public class ProfileDTO {
    private String profileImageUrl;
    private String username;
    private String userType;
    private String biography;

    public ProfileDTO(String profileImageUrl, String username, String userType, String biography) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.userType = userType;
        this.biography = biography;
    }

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}  
}
