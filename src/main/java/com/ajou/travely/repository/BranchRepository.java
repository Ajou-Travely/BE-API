package com.ajou.travely.repository;

import com.ajou.travely.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("select b from Branch b where b.schedule.id = :scheduleId and b.user.id = :userId")
    public Optional<Branch> findByScheduleIdAndUserId(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId);
}
