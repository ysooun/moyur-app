package com.moyur.profile.imageupload;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class UploadController {
    
    private final UploadService uploadService;
    private final UploadRepository uploadRepository;

    public UploadController(UploadService uploadService, UploadRepository uploadRepository) {
        this.uploadService = uploadService;
        this.uploadRepository = uploadRepository;
    }

    @PostMapping(value = "/imageUpload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImage(
            @RequestPart("uploadDTO") UploadDTO uploadDTO,
            @RequestPart("file") MultipartFile file) {
        try {
            UploadEntity uploadEntity = uploadService.uploadImage(uploadDTO.getUsername(), file, uploadDTO.getImageType());
            return new ResponseEntity<>(uploadEntity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Failed to upload image. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@GetMapping("/images/{username}")
	public ResponseEntity<List<String>> getImages(@PathVariable String username) {
	    List<UploadEntity> images = uploadRepository.findAllByUser_Username(username);
	    List<String> imageUrls = images.stream()
	        .map(UploadEntity::getImageUrl)
	        .collect(Collectors.toList());
	            
	    return new ResponseEntity<>(imageUrls, HttpStatus.OK);
	}
}