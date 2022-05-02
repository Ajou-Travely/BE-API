package com.ajou.travely.repository;

import com.ajou.travely.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select s from Schedule s join fetch s.place where s.id = :scheduleId")
    public Optional<Schedule> findScheduleByIdWithPlace(@Param("scheduleId") Long scheduleId);

    @Query("select s from Schedule s join s.place p where p.id = :placeId")
    public List<Schedule> findSchedulesByPlaceId(@Param("placeId") Long placeId);

    @Query("select s from Schedule s join s.place p where p.placeName = :placeName")
    public List<Schedule> findSchedulesByPlaceName(@Param("placeName") String placeName);

    @Query("select s from Schedule s join s.travel t where t.id = :travelId")
    public List<Schedule> findSchedulesByTravelId(@Param("travelId") Long travelId);

    @Query("select s from Schedule s join s.travel t join fetch s.place where t.id = :travelId")
    public List<Schedule> findSchedulesWithPlaceByTravelId(@Param("travelId") Long travelId);
}