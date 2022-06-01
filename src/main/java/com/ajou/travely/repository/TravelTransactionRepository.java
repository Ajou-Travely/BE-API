package com.ajou.travely.repository;

import com.ajou.travely.domain.cost.TravelTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelTransactionRepository extends JpaRepository<TravelTransaction, Long> {
    List<TravelTransaction> findBySenderId(Long senderId);
    List<TravelTransaction> findByReceiverId(Long receiverId);
}
