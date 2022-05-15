package com.ajou.travely.repository;

import com.ajou.travely.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("select p from Place p where p.placeName like %:name%")
    List<Place> findByName(@Param("name") String name);

    Optional<Place> findByKakaoMapId(Long kakaoMapId);
}
