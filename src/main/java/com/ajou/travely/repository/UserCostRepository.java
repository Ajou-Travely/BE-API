package com.ajou.travely.repository;

import com.ajou.travely.domain.UserCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCostRepository extends JpaRepository<UserCost, Long> {
    @Modifying
    @Query("delete " +
            "from UserCost uc " +
            "where uc.cost.id = :costId and uc.user.id = :userId")
    void deleteUserCostByCostIdAndUserId(@Param("costId") Long costId, @Param("userId") Long userId);

    @Modifying
    @Query("update " +
            "UserCost uc " +
            "set uc.amount = :amount, " +
            "uc.isRequested = :isRequested " +
            "where uc.id = :userCostId")
    void updateUserCostByUserCostId(
            @Param("amount") Long amount,
            @Param("isRequested") Boolean isRequested,
            @Param("userCostId") Long userCostId
    );

}
