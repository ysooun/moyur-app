package com.moyur.profile;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.moyur.aws.S3Service;
import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@Service
public class ProfileService {
	
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, S3Service s3Service) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }
    
    public List<ProfileEntity> getAllProfiles() {
        return profileRepository.findAll();
    }
    
    public ProfileEntity getProfileByUsername(String username) {
        return profileRepository.findByUser_Username(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void createProfileIfNotExist(String username) {
        if (!profileRepository.existsByUser_Username(username)) {
            UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            createProfile(userEntity);
        }
    }
    
    private ProfileEntity createProfile(UserEntity userEntity) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUser(userEntity);
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);
        return savedProfileEntity;
    }
    
    @Transactional
    public String uploadProfile(String username, MultipartFile profileImageFile, String userType, String biography) {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // 프로필 찾거나 생성
        ProfileEntity profileEntity = profileRepository.findByUser_Username(username)
            .orElseGet(() -> createProfile(userEntity));

        // 이미지 파일이 제공된 경우 S3에 업로드
        String profileImageUrl = null;
        if (profileImageFile != null) {
            try {
                profileImageUrl = s3Service.s3Upload(profileImageFile);
                profileEntity.setProfileImageUrl(profileImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }
        
        // 사용자 유형 정보가 제공된 경우 업데이트
        if (userType != null) {
            profileEntity.setUserType(UserType.valueOf(userType));
        }

        // 자기소개 정보가 제공된 경우 업데이트
        if (biography != null) {
            profileEntity.setBiography(biography);
        }
        
        // 프로필 저장
        profileRepository.save(profileEntity);
        
        return profileImageUrl;
    }
    
    @Transactional(readOnly = true)
    public String getBiography(String username) {
        return profileRepository.findByUser_Username(username)
                .map(ProfileEntity::getBiography)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
