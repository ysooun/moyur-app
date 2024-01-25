package com.moyur.cafe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moyur.repository.UserRepository;

@Service
public class CafeService {

    private final CafeRepository cafeRepository;

    public CafeService(CafeRepository cafeRepository, UserRepository userRepository) {
        this.cafeRepository = cafeRepository;
    }

    public List<CafeDTO> getAllCafes() {
        List<CafeEntity> cafes = cafeRepository.findAll();
        return cafes.stream()
                .map(cafe -> new CafeDTO(cafe.getId(), cafe.getImageUrl(), cafe.getLikes(), cafe.getUser().getUsername()))
                .collect(Collectors.toList());
    }
}
