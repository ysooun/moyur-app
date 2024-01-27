package com.moyur.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.moyur.aws.S3Service;
import com.moyur.cafe.CafeDTO;
import com.moyur.cafe.CafeRepository;
import com.moyur.cafe.CafeService;
import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CafeServiceTest {

    @Mock
    private CafeRepository cafeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CafeService cafeService;

    @Test
    public void testUploadImageAndSaveToDatabase() throws IOException {
        // 테스트용 사용자 ID, 좋아요 수, MultipartFile
        Long userId = 1L;
        int likes = 10;

        // MockMultipartFile 생성
        MultipartFile file = new MockMultipartFile(
                "testFile",
                "test.txt",
                "text/plain",
                "test data".getBytes()
        );

        // 테스트용 사용자 엔터티
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        // 필요한 사용자 엔터티 설정...

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(s3Service.uploadFile(file)).thenReturn("mocked-s3-url");

        // 테스트 수행
        CafeDTO result = cafeService.uploadImageAndSaveToDatabase(file, userId, likes);

        // 결과 확인
        assertNotNull(result);
        // 적절한 결과 검증...

        // Mock 메서드 호출 검증
        verify(userRepository, times(1)).findById(userId);
        verify(s3Service, times(1)).uploadFile(file);
        verify(cafeRepository, times(1)).save(any());
    }
}
