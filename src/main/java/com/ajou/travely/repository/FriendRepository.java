package com.ajou.travely.repository;

import com.ajou.travely.domain.Friend;
import com.ajou.travely.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFolloweeAndFollower(User followee, User follower);

    @Query(value = "select f from Friend f left join fetch f.follower fwer where f.followee.id = :followeeId and f.isFriend = true")
    List<Friend> findAllFriendsByFollowee(@Param("followeeId") Long followeeId);

    @Query(value = "select f from Friend f left join fetch f.followee fwee where f.follower.id = :followerId and f.isFriend = false ")
    List<Friend> findGivenRequestsByFollower(@Param("followerId") Long followerId);

    @Query(value = "select f from Friend f left join fetch f.follower fwer where f.followee.id = :followeeId and f.isFriend = false")
    List<Friend> findGivingRequestsByFollowee(@Param("followeeId") Long followeeId);

    @Modifying
    @Query("delete from Friend f where f.followee.id = :followeeId and f.follower.id = :followerId")
    void deleteByFolloweeIdAndFollowerId(@Param("followeeId") Long followeeId,
                                         @Param("followerId") Long followerId);
}
