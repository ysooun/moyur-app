package com.moyur.cafe;

public class CafeDTO {
    private Long id;
    private String imageUrl;
    private int likes;
    private String username; // 사용자의 유저네임

    // 생성자, getter 및 setter 메서드

    public CafeDTO() {
        // 기본 생성자
    }

    public CafeDTO(Long id, String imageUrl, int likes, String username) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.username = username;
    }

    // getter 및 setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
