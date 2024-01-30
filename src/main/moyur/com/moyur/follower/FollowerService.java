package com.moyur.follower;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moyur.jwt.UserEntity;
import com.moyur.jwt.UserRepository;

@Service
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    // 생성자에 @Autowired를 사용하여 FollowerRepository와 UserRepository 주입
    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public void follow(String username, String followerUsername) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        UserEntity follower = userRepository.findByUsername(followerUsername)
            .orElseThrow(() -> new IllegalArgumentException("Follower not found: " + followerUsername));

        FollowerEntity followerEntity = new FollowerEntity();
        followerEntity.setUser(user);
        followerEntity.setFollower(follower);
        followerRepository.save(followerEntity);
    }

    public void unfollow(String username, String followerUsername) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        UserEntity follower = userRepository.findByUsername(followerUsername)
            .orElseThrow(() -> new IllegalArgumentException("Follower not found: " + followerUsername));

        FollowerEntity followerEntity = followerRepository.findByUserAndFollower(user, follower)
            .orElseThrow(() -> new IllegalArgumentException("Follower not found"));
        followerRepository.delete(followerEntity);
    }

    public List<FollowerEntity> getFollowers(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return followerRepository.findByUser(user);
    }

    public List<FollowerEntity> getFollowings(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return followerRepository.findByFollower(user);
    }
    
    public int getFollowerCount(String username) {
        return followerRepository.countByUser_Username(username);
    }
    
    public boolean checkFollowExists(String targetUsername, String loggedInUsername) {
        UserEntity user = userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + targetUsername));
        UserEntity follower = userRepository.findByUsername(loggedInUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loggedInUsername));
        return followerRepository.existsByUserAndFollower(user, follower);
    }
}

