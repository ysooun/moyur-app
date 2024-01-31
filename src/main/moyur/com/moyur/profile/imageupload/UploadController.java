package com.moyur.profile.imageupload;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class UploadController {
    
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/imageUpload")
    public CompletableFuture<ResponseEntity<UploadEntity>> uploadImage(
            @RequestParam String username,
            @RequestParam("file") MultipartFile file,
            @RequestParam String imageType) {

        return uploadService.uploadImage(username, file, imageType)
                .thenApply(uploadEntity -> 
                    new ResponseEntity<>(uploadEntity, HttpStatus.CREATED))
                .exceptionally(e -> {
                    if (e.getCause() instanceof IOException) {
                        // 파일 업로드 중 오류 발생
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    } else if (e.getCause() instanceof UsernameNotFoundException) {
                        // 사용자를 찾을 수 없음
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    } else {
                        // 그 외의 예외
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });
    }
}