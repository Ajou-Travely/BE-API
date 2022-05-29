package com.ajou.travely.repository;

import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.domain.travel.TravelDateIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TravelDateRepository extends JpaRepository<TravelDate, TravelDateIds> {
    @Query("select distinct s " +
            "from Schedule s " +
            "join fetch s.place " +
            "where s.travelDate.travel.id = :travelId " +
            "and s.travelDate.travelDateIds.date = :date")
    List<Schedule> findSchedulesWithPlaceByDateAndTravelId(@Param("date") LocalDate date, @Param("travelId") Long travelId);
    @Query("select td " +
            "from TravelDate td " +
            "where td.travel.id = :travelId " +
            "and td.travelDateIds.date = :date")
    Optional<TravelDate> findTravelDateByDateAndTravelId(@Param("date") LocalDate date, @Param("travelId") Long travelId);
}
