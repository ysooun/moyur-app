package com.moyur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moyur.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}