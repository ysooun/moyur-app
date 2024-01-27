package com.moyur.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final String bucketName = "your-bucket-name";

    public S3Service(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    // 이미지 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        File convertedFile = convertMultiPartToFile(file);
        String fileName = file.getOriginalFilename();
        String s3ImageUrl = uploadToS3(fileName, convertedFile);
        convertedFile.delete(); // S3 업로드 후 로컬 파일 삭제
        return s3ImageUrl;
    }

    // 이미지 다운로드
    public byte[] downloadFile(String imageName) throws IOException {
        S3Object object = amazonS3Client.getObject(bucketName, imageName);
        try (S3ObjectInputStream objectContent = object.getObjectContent()) {
            return objectContent.readAllBytes();
        }
    }

    private String uploadToS3(String fileName, File file) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}