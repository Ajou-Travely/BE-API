package com.ajou.travely.service;

import com.ajou.travely.domain.Invitation;
import com.ajou.travely.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;

    @Transactional
    public Invitation insertInvitation(Invitation invitation) {
        return invitationRepository.save(invitation);
    }
}
