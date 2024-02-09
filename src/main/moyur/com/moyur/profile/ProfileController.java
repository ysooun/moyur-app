package com.moyur.profile;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public String showUserProfile(@PathVariable String username, Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUsername = userDetails.getUsername();
            String role = userDetails.getAuthorities().stream().findFirst().orElseThrow(() -> new IllegalStateException("No authorities found")).getAuthority();

            if (role.equals("ROLE_USER")) {
                // 로그인한 사용자와 요청한 사용자가 일치하는지 확인
                boolean isMyProfile = loggedInUsername.equals(username);
                model.addAttribute("isMyProfile", isMyProfile);

                // 로그인한 사용자와 요청한 사용자가 일치할 때만 프로필 생성
                if (isMyProfile) {
                    profileService.createProfileIfNotExist(username);
                }

                ProfileEntity profileEntity = profileService.getProfileByUsername(username);

                model.addAttribute("username", username);
                model.addAttribute("profileImageUrl", profileEntity.getProfileImageUrl());
                return "profile";
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> updateProfile(@RequestPart(name = "profileDTO", required = false) ProfileDTO profileDTO,
                                           @RequestPart(name = "image", required = false) MultipartFile profileImage,
                                           @RequestPart(name = "biography", required = false) String biography) {
        try {
            String username = profileDTO.getUsername();
            String userType = profileDTO.getUserType();
            String newImageUrl = profileService.uploadProfile(username, profileImage, userType, biography);
            return new ResponseEntity<>(Collections.singletonMap("newImageUrl", newImageUrl), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Failed to update profile. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getBio")
    public ResponseEntity<?> getBiography(@RequestParam String username) {
        String biography = profileService.getBiography(username);
        return ResponseEntity.ok(Collections.singletonMap("biography", biography));
    }
}
