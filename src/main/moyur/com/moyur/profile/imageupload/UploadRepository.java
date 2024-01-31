package com.moyur.profile.imageupload;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<UploadEntity, Long> {
	Optional<UploadEntity> findByUser_Username(String username);
}