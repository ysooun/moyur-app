package com.moyur.join;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String confirmPassword = joinDTO.getConfirmPassword();
        String email = joinDTO.getEmail();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new RuntimeException("중복된 아이디입니다.");
        }
        
        if (!PasswordValidator.validate(password)) {
            throw new RuntimeException("8자리 이상, 영문 포함, 특수문자 1개 이상 사용해야 합니다.");
        }
        
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        if (!EmailValidator.validate(email)) {
            throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setEmail(email);
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }
}