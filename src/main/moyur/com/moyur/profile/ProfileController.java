package com.moyur.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

	@GetMapping("/profile")
    public ResponseEntity<?> showUserProfile(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream().findFirst().orElseThrow(() -> new IllegalStateException("No authorities found")).getAuthority();

            // 권한이 있는 경우 프로필 템플릿을 보여줌
            if (role.equals("ROLE_USER")) { // 권한에 따라 조건을 수정하세요
                // 프로필 템플릿 보여주는 코드 추가
                return ResponseEntity.ok("Profile accessed with role: " + role);
            } else {
                // 다른 권한의 경우에도 처리 가능
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access forbidden for the role: " + role);
            }
        } else {
            // 권한이 없는 경우 로그인 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}