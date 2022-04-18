package com.ajou.travely.repository;

import com.ajou.travely.domain.Travel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("SELECT t FROM Travel t ORDER BY t.id DESC")
    List<Travel> findAllDesc();
}
