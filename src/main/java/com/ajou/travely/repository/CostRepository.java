package com.ajou.travely.repository;

import com.ajou.travely.domain.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostRepository extends JpaRepository<Cost, Long> {
}
