package com.ajou.travely.repository;

import com.ajou.travely.domain.UserTravel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelRepository extends JpaRepository<UserTravel, Long> {
}
