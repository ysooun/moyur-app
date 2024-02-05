package com.moyur.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUser_Username(String username);
    boolean existsByUser_Username(String username);
}