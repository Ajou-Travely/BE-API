package com.ajou.travely.repository;

import com.ajou.travely.controller.CostsResponseDto;
import com.ajou.travely.domain.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    @Query("select c, ucs from Cost c join c.travel t join fetch c.userCosts ucs where t.id = :travelId")
    public List<CostsResponseDto> test();
}
