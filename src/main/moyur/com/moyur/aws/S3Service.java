package com.moyur.aws;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Config s3Config;

    public S3Service(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String s3Upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = s3Config.getLocalLocation() + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);

        s3Config.amazonS3Client().putObject(
                new PutObjectRequest(bucket, uuidFileName, localFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        localFile.delete();

        return s3Url;
    }
}