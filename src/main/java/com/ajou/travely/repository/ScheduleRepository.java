package com.ajou.travely.repository;

import com.ajou.travely.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select s from Schedule s join fetch s.branches b join fetch b.user join fetch s.place where s.id = :scheduleId")
    Optional<Schedule> findScheduleWithUsersAndPlaceByScheduleId(@Param("scheduleId") Long scheduleId);
}