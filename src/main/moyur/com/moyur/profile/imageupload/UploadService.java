package com.moyur.profile.imageupload;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moyur.aws.S3Service;
import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UploadService {
    private final UserRepository userRepository;
    @Lazy
    private final UploadRepository uploadRepository;
    private final S3Service s3Service;

    // 생성자 주입
    public UploadService(UserRepository userRepository, @Lazy UploadRepository uploadRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.uploadRepository = uploadRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public UploadEntity uploadImage(String username, MultipartFile imageFile, String imageType) {
        try {
            // 이미지 파일을 S3에 업로드하고, 업로드된 이미지의 URL을 가져옴
            String imageUrl;
            try {
                imageUrl = s3Service.s3Upload(imageFile); 
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }

            // 이미지 정보 생성
            UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            UploadEntity uploadEntity = new UploadEntity();
            uploadEntity.setUser(userEntity);
            uploadEntity.setImageUrl(imageUrl);
            uploadEntity.setImageType(imageType);

            // 이미지 정보 저장
            uploadRepository.save(uploadEntity);

            return uploadEntity;

        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found: " + username, e);
        }
    }

}
