package com.ajou.travely.repository;

import com.ajou.travely.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findById(Long id);

    Travel save(Travel travel);
}
