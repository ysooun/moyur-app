package com.moyur.join;

import java.util.regex.Pattern;

public class PasswordValidator {

    // 비밀번호 패턴: 8자리 이상, 영문 포함, 특수문자 1개 이상 사용
    private static final String PASSWORD_PATTERN = 
            "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+=-])(?=\\S+$).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean validate(final String password) {
        return pattern.matcher(password).matches();
    }
}