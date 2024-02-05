package com.moyur.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	// 인증 관리자 및 JWT 유틸리티
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	
	// 생성자에서 인증 관리자와 JWT 유틸리티를 초기화
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		   this.authenticationManager = authenticationManager;
		   this.jwtUtil = jwtUtil;
		}

	// 사용자의 인증 시도를 처리하는 메서드
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
	   try {
	       // 요청에서 사용자 정보를 가져와서 UserEntity 객체로 변환
	       UserEntity users = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
	       String username = users.getUsername();
	       String password = users.getPassword();
	
	       // UsernamePasswordAuthenticationToken 객체를 생성하여 인증 시도
	       UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
	       return authenticationManager.authenticate(authToken);
	   } catch (IOException e) {
	       throw new RuntimeException(e);
	   }
	}
	
	// 인증이 성공했을 때 호출되는 메서드
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
	    Object principal = authentication.getPrincipal();

	    if (principal instanceof CustomUserDetails) {
	        CustomUserDetails customUserDetails = (CustomUserDetails) principal;
	        String username = customUserDetails.getUsername();
	        

	        // 사용자의 권한을 가져옴
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
	        GrantedAuthority auth = iterator.next();
	        String role = auth.getAuthority();

	        String token = jwtUtil.createJwt(username, role, 2 * 24 * 60 * 60 * 1000L);

	        Cookie cookie = new Cookie("Authorization", token);
	        cookie.setHttpOnly(true);
	        cookie.setMaxAge(1 * 24 * 60 * 60); // 초 단위로 설정
	        cookie.setPath("/");

	        response.addCookie(cookie);  // 응답에 쿠키 추가
	    } else {
	    }
	}
	
	// 인증이 실패했을 때 호출되는 메서드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
	   // HTTP 상태를 UNAUTHORIZED로 설정하고, 실패 메시지를 출력
	   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	   response.setContentType("text/plain; charset=UTF-8");
	   try {
	       response.getWriter().write("아이디 또는 패스워드가 일치하지 않습니다.");
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	}
}