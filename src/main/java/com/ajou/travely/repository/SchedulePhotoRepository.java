package com.ajou.travely.repository;

import com.ajou.travely.domain.SchedulePhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SchedulePhotoRepository extends JpaRepository<SchedulePhoto, Long> {
    @Query("select sp from SchedulePhoto sp where sp.schedule.id = :schedule_id")
    List<SchedulePhoto> findSchedulePhotosByScheduleIdInQuery(@Param("schedule_id") Long schedule_id);

    @Query("select sp from SchedulePhoto sp where sp.schedule.id in :schedule_ids")
    List<SchedulePhoto> findSchedulePhotosByScheduleIdsInQuery(@Param("schedule_ids") List<Long> schedule_ids);
}
