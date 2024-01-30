package com.moyur.profile;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile/{username}")
    public String showUserProfile(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream().findFirst().orElseThrow(() -> new IllegalStateException("No authorities found")).getAuthority();

            // 권한이 있는 경우 프로필 템플릿을 보여줌
            if (role.equals("ROLE_USER")) {
                // 권한에 따라 프로필 정보를 가져오는 로직이나 다른 필요한 로직을 추가하세요.
                String username = userDetails.getUsername();

                // 모델에 데이터 추가
                model.addAttribute("username", username);
                model.addAttribute("role", role);

                // 프로필 템플릿을 보여주는 Thymeleaf 템플릿 경로를 리턴
                return "profile";
            } else {
                // 다른 권한의 경우에도 처리 가능
                return "forbidden";
            }
        } else {
            // 권한이 없는 경우 로그인 페이지로 리다이렉트
            return "unauthorized";
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileEntity> createProfile(@RequestParam String username,
                                                      @RequestParam MultipartFile profileImageUrl,
                                                      @RequestParam String userType) {
        ProfileEntity profile = profileService.createProfile(username, profileImageUrl, userType);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestPart("profileDTO") ProfileDTO profileDTO,
                                           @RequestPart("image") MultipartFile profileImage) {
        try {
            // 클라이언트로부터 전송된 데이터 가져오기
            String username = profileDTO.getUsername();
            String userType = profileDTO.getUserType();

            // 데이터베이스에 이미지 정보 저장하기
            profileService.updateProfile(username, profileImage, userType);

            // 성공적으로 저장된 경우 응답
            return new ResponseEntity<>(Collections.singletonMap("message", "Profile updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            // 실패한 경우 에러 응답
            return new ResponseEntity<>(Collections.singletonMap("message", "Failed to update profile. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}