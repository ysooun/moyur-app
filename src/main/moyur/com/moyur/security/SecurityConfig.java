package com.moyur.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.moyur.jwt.JWTFilter;
import com.moyur.jwt.JWTUtil;
import com.moyur.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	
	private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
	
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
	
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                		.requestMatchers("/profile/**").authenticated()
                		.requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());
        http	
        		.formLogin((formLogin) -> formLogin
		                .loginPage("/login")
		                .defaultSuccessUrl("/"))
        		.logout((logout) -> logout
		                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		                .logoutSuccessUrl("/"));
        http
        		.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        
        http
        		.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }  
}