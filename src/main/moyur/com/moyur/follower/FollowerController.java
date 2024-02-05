package com.moyur.follower;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/followers")
public class FollowerController {
    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/{targetUsername}/{loggedInUsername}")
    public ResponseEntity<Void> follow(@PathVariable String targetUsername, @PathVariable String loggedInUsername) {
        followerService.follow(targetUsername, loggedInUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUsername}/{loggedInUsername}")
    public ResponseEntity<Void> unfollow(@PathVariable String targetUsername, @PathVariable String loggedInUsername) {
        followerService.unfollow(targetUsername, loggedInUsername);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<FollowerEntity>> getFollowers(@PathVariable String username) {
        List<FollowerEntity> followers = followerService.getFollowers(username);
        return ResponseEntity.ok(followers);
    }
    
    @GetMapping("/{username}/followerCount")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable String username) {
        int followerCount = followerService.getFollowerCount(username);
        return ResponseEntity.ok(followerCount);
    }

    @GetMapping("/exists/{targetUsername}/{loggedInUsername}")
    public ResponseEntity<Map<String, Boolean>> checkFollowExists(
            @PathVariable String targetUsername, 
            @PathVariable String loggedInUsername) {
        boolean exists = followerService.checkFollowExists(targetUsername, loggedInUsername);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}


