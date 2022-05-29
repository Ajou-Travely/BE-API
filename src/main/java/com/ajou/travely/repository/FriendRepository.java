package com.ajou.travely.repository;

import com.ajou.travely.domain.Friend;
import com.ajou.travely.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFolloweeAndFollower(User followee, User follower);

    @Query(value = "select distinct f from Friend f left join fetch f.follower fwer where f.followee.id = :followeeId and f.isFriend = true",
    countQuery = "select count(f) from Friend f where f.followee.id = :followeeId and f.isFriend = true")
    Page<Friend> findAllFriendsByFollowee(@Param("followeeId") Long followeeId, Pageable pageable);

    @Query(value = "select distinct f from Friend f left join fetch f.followee fwee where f.follower.id = :followerId and f.isFriend = false",
    countQuery = "select count(f) from Friend f where f.follower.id = :followerId and f.isFriend = false ")
    Page<Friend> findGivenRequestsByFollower(@Param("followerId") Long followerId, Pageable pageable);

    @Query(value = "select distinct f from Friend f left join fetch f.follower fwer where f.followee.id = :followeeId and f.isFriend = false",
    countQuery = "select count(f) from Friend f where f.followee.id = :followeeId and f.isFriend = false ")
    Page<Friend> findGivingRequestsByFollowee(@Param("followeeId") Long followeeId, Pageable pageable);

    @Modifying
    @Query("delete from Friend f where f.followee.id = :followeeId and f.follower.id = :followerId")
    void deleteByFolloweeIdAndFollowerId(@Param("followeeId") Long followeeId,
                                         @Param("followerId") Long followerId);
}
