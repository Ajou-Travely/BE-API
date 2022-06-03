package com.ajou.travely.repository;

import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.kakaoId = :kakaoId")
    Optional<User> findByKakaoId(@Param("kakaoId") Long kakaoId);

    @Query("select " +
            "distinct u " +
            "from Travel t " +
            "join t.userTravels uts " +
            "join uts.user u " +
            "where t.id = :travelId")
    List<User> findUsersByTravelId(@Param("travelId") Long travelId);

    @Query("select t from Travel t join t.userTravels ut join ut.user u where u.id = :userId")
    Page<Travel> findTravelsByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
