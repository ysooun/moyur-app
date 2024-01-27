package com.moyur.main;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        // 서버 시스템 시간대 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}