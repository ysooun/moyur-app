package com.moyur.cafe;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moyur.aws.S3Service;
import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public CafeService(CafeRepository cafeRepository, UserRepository userRepository, S3Service s3Service) {
        this.cafeRepository = cafeRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    public List<CafeDTO> getAllCafes() {
        List<CafeEntity> cafes = cafeRepository.findAll();
        return cafes.stream()
                .map(cafe -> new CafeDTO(cafe.getId(), cafe.getImageUrl(), cafe.getLikes(), cafe.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    public CafeDTO uploadImageAndSaveToDatabase(MultipartFile file, Long userId, int likes) {
        try {
            // 이미지를 S3에 업로드하고 S3 URL을 얻기
            String s3ImageUrl = s3Service.uploadFile(file);

            // 사용자 ID로 사용자 엔터티 가져오기
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            // 새로운 카페 엔터티 생성 및 저장
            CafeEntity cafeEntity = new CafeEntity();
            cafeEntity.setImageUrl(s3ImageUrl);
            cafeEntity.setLikes(likes);
            cafeEntity.setUser(userEntity);

            CafeEntity savedCafe = cafeRepository.save(cafeEntity);

            return new CafeDTO(savedCafe.getId(), savedCafe.getImageUrl(), savedCafe.getLikes(), savedCafe.getUser().getUsername());
        } catch (IOException e) {
            e.printStackTrace(); // 적절하게 예외를 처리하세요.
            return null;
        }
    }
}