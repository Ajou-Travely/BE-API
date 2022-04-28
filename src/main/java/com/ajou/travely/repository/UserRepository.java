package com.ajou.travely.repository;

import com.ajou.travely.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.kakaoId = :kakaoId")
    public Optional<User> findByKakaoId(@Param("kakaoId") Long kakaoId);
}
