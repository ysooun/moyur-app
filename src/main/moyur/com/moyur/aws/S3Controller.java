package com.moyur.aws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class S3Controller {

    private S3Service s3Service;

    public S3Controller(S3Service s3Service) {

        this.s3Service = s3Service;
    }

    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String, Object> imageUpload(@RequestParam("upload") MultipartFile file) {

        Map<String, Object> responseData = new HashMap<>();

        try {
            String s3Url = s3Service.s3Upload(file);

            responseData.put("uploaded", true);
            responseData.put("url", s3Url);

            return responseData;

        } catch (IOException e) {

            responseData.put("uploaded", false);

            return responseData;
        }
    }
}