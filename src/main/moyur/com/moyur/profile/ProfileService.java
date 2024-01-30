package com.moyur.profile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moyur.aws.S3Service;
import com.moyur.jwt.CustomUserDetails;
import com.moyur.jwt.CustomUserDetailsService;
import com.moyur.jwt.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	
    private final ProfileRepository profileRepository;
    private final CustomUserDetailsService userDetailsService;
    private final S3Service s3Service;
    
    public ProfileService(ProfileRepository profileRepository, CustomUserDetailsService userDetailsService, S3Service s3Service) {
        this.profileRepository = profileRepository;
        this.userDetailsService = userDetailsService;
        this.s3Service = s3Service;
    }
    
    public ProfileEntity createProfile(String username, MultipartFile profileImageFile, String userType) {
        CustomUserDetails userDetails = getProfileByUsername(username);

        UserEntity userEntity = userDetails.getUserEntity();

        // S3에 이미지 업로드
        String profileImageUrl;
        try {
            profileImageUrl = s3Service.s3Upload(profileImageFile);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
        
        // 프로필 생성
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUser(userEntity);
        profileEntity.setProfileImageUrl(profileImageUrl);
        profileEntity.setUserType(UserType.NORMAL);

        // 프로필 저장
        return profileRepository.save(profileEntity);
    }
  
    public List<ProfileEntity> getAllProfiles() {
        return profileRepository.findAll();
    }

    public CustomUserDetails getProfileByUsername(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails instanceof CustomUserDetails) {
            return (CustomUserDetails) userDetails;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public String uploadProfileImage(MultipartFile image) {
        try {
            return s3Service.s3Upload(image);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
    
    @Transactional
    public void updateProfile(String username, MultipartFile profileImageFile, String userType) {
        CustomUserDetails userDetails = getProfileByUsername(username);

        UserEntity userEntity = userDetails.getUserEntity();
        
        // S3에 이미지 업로드
        String profileImageUrl;
        try {
            profileImageUrl = s3Service.s3Upload(profileImageFile); 
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
        
        // 기존 프로필 찾기
        Optional<ProfileEntity> optionalProfileEntity = profileRepository.findByUser_Username(username);
        
        ProfileEntity profileEntity;
        // 프로필이 없는 경우 새로 생성
        if (optionalProfileEntity.isPresent()) {
            profileEntity = optionalProfileEntity.get();
        } else {
            profileEntity = new ProfileEntity();
            profileEntity.setUser(userEntity);
        }
        
        // 프로필 정보 업데이트
        profileEntity.setProfileImageUrl(profileImageUrl);
        profileEntity.setUserType(UserType.NORMAL);
        
        // 프로필 저장
        profileRepository.save(profileEntity);
    }
}