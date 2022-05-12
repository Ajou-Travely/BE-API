package com.ajou.travely.repository;

import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("SELECT t FROM Travel t ORDER BY t.id DESC")
    List<Travel> findAllDesc();

    @Query("select distinct s from Schedule s join fetch s.place where s.travel.id = :travelId")
    List<Schedule> findSchedulesWithPlaceByTravelId(@Param("travelId") Long travelId);
}
