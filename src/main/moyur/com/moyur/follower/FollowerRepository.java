package com.moyur.follower;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moyur.jwt.UserEntity;

public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {
    List<FollowerEntity> findByUser(UserEntity user);
    List<FollowerEntity> findByFollower(UserEntity follower);
    Optional<FollowerEntity> findByUserAndFollower(UserEntity user, UserEntity follower);
    int countByUser_Username(String username);
    boolean existsByUserAndFollower(UserEntity user, UserEntity follower);
}