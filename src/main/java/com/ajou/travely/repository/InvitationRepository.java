package com.ajou.travely.repository;

import com.ajou.travely.domain.Invitation;
import com.ajou.travely.domain.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByCodeAndEmail(UUID code, String email);
    Optional<Invitation> findByTravelIdAndEmail(Long travelId, String email);
}
