package com.moyur.profile.imageupload;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moyur.aws.S3Service;
import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@Service
public class UploadService {
    private final UserRepository userRepository;
    private final UploadRepository uploadRepository;
    private final S3Service s3Service;

    // 생성자 주입
    public UploadService(UserRepository userRepository, UploadRepository uploadRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.uploadRepository = uploadRepository;
        this.s3Service = s3Service;
    }

    @Async
    public CompletableFuture<UploadEntity> uploadImage(String username, MultipartFile imageFile, String imageType) {
        try {
            // 이미지 파일을 S3에 업로드하고, 업로드된 이미지의 URL을 가져옴
            String imageUrl;
            try {
                imageUrl = s3Service.s3Upload(imageFile); 
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }

            // 이미지 정보 생성
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (!userOptional.isPresent()) {
                // 사용자를 찾지 못한 경우의 처리
                throw new UsernameNotFoundException("User not found: " + username);
            }

            UserEntity user = userOptional.get();
            UploadEntity uploadEntity = new UploadEntity();
            uploadEntity.setUser(user);
            uploadEntity.setImageUrl(imageUrl);
            uploadEntity.setImageType(imageType);

            // 이미지 정보 저장
            uploadRepository.save(uploadEntity);

            return CompletableFuture.completedFuture(uploadEntity);

        } catch (UsernameNotFoundException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
