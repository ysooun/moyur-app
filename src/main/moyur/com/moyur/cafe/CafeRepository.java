package com.moyur.cafe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends JpaRepository<CafeEntity, Long> {
    List<CafeEntity> findByUserUsername(String username);
}