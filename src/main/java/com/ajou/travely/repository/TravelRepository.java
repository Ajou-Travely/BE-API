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

    @Query("select t from Travel t join fetch t.userTravels ut join fetch ut.user")
    List<Travel> findAllTravelsWithUser();

    @Query("select distinct t from Travel t join fetch t.userTravels ut join fetch ut.user where t.id = :travelId")
    Optional<Travel> findTravelWithUsersById(@Param("travelId") Long travelId);

    @Query("select distinct s from Schedule s join fetch s.place join fetch s.branches join s.travel t where t.id = :travelId")
    List<Schedule> findSchedulesByTravelId(@Param("travelId") Long travelId);
}
