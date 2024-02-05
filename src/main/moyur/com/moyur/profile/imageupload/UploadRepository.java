package com.moyur.profile.imageupload;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<UploadEntity, Long> {
	Optional<UploadEntity> findByUser_Username(String username);
	List<UploadEntity> findAllByUser_Username(String username);
}