package com.ajou.travely.repository;

import com.ajou.travely.controller.travel.dto.CostsResponseDto;
import com.ajou.travely.domain.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    @Query("select " +
            "distinct c " +
            "from Cost c " +
            "join c.travel t " +
            "join fetch c.userCosts " +
            "where t.id = :travelId")
    public List<Cost> findCostsByTravelId(@Param("travelId") Long travelId);
}
