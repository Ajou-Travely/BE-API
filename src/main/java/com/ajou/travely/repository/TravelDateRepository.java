package com.ajou.travely.repository;

import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.TravelDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TravelDateRepository extends JpaRepository<TravelDate, Long> {
    @Query("select distinct s from Schedule s join fetch s.place where s.travelDate.id = :travelDateId")
    List<Schedule> findSchedulesWithPlaceByTravelDateId(@Param("travelDateId") Long travelDateId);
}
