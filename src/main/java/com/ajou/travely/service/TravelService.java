package com.ajou.travely.service;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.*;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    private final UserRepository userRepository;

    private final UserTravelRepository userTravelRepository;

    private final CostRepository costRepository;

    private final InvitationRepository invitationRepository;

    private final CustomMailSender customMailSender;

    @Value("${domains.front-domain}")
    private String frontDomain;

    @Transactional
    public Travel insertTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    @Transactional
    public Travel createTravel(Long userId, TravelCreateRequestDto travelCreateRequestDto) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User가 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                ));
        Travel travel = travelRepository.save(
                Travel.builder()
                        .title(travelCreateRequestDto.getTitle())
                        .startDate(travelCreateRequestDto.getStartDate())
                        .endDate(travelCreateRequestDto.getEndDate())
                        .managerId(userId)
                        .build());
        UserTravel userTravel = UserTravel.builder().user(user).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
        return travel;
    }

    @Transactional
    public List<SimpleTravelResponseDto> getAllTravels() {
        return travelRepository.
                findAll().
                stream().
                map(SimpleTravelResponseDto::new).
                collect(Collectors.toList());
    }

    @Transactional
    public void inviteUserToTravel(Long travelId, TravelInviteRequestDto travelInviteRequestDto) {
        Travel travel = travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        // TODO email 검증
        UUID code = UUID.randomUUID();
        String text = frontDomain + "invite/accept/" + code;
        customMailSender.sendInvitationEmail(
                travelInviteRequestDto.getEmail(),
                text
        );
        invitationRepository
                .save(
                        new Invitation(
                                travelInviteRequestDto
                                        .getEmail()
                                , travel
                                , code
                        )
                );
    }

    @Transactional
    public void inviteUserToTravelWithNoValidation(Travel travel, String email) {
        // TODO email 검증
        UUID code = UUID.randomUUID();
        String text = frontDomain + "invite/accept/" + code;
        customMailSender.sendInvitationEmail(
                email,
                text
        );
        invitationRepository
                .save(
                        new Invitation(
                                email
                                , travel
                                , code
                        )
                );
    }

    @Transactional
    public Long addUserToTravelWithValidation(Long userId, UUID code) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                ));
        Invitation invitation = invitationRepository
                .findByCodeAndEmail(code, user.getEmail())
                .orElseThrow(() -> new RecordNotFoundException(
                        "잘못된 초대 링크입니다."
                        , ErrorCode.INVALID_INVITATION
                ));
        Travel travel = travelRepository
                .findById(invitation.getTravel().getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        invitationRepository.deleteById(invitation.getId());
        UserTravel userTravel = UserTravel.builder().user(user).travel(travel).build();
        userTravelRepository.save(userTravel);
        return travel.getId();
    }

    @Transactional
    public void addUserToTravel(Long travelId, Long userId) {
        Travel travel = travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                ));
        UserTravel userTravel = UserTravel.builder().user(user).travel(travel).build();
        userTravelRepository.save(userTravel);
    }

    @Transactional
    public TravelResponseDto getTravelById(Long travelId) {
        Travel travel = travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        List<Schedule> schedules = travelRepository.findSchedulesWithPlaceByTravelId(travel.getId());
        return new TravelResponseDto(travel, schedules);
    }

    @Transactional
    public List<User> getUsersOfTravel(Long travelId) {
        return travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ))
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SimpleUserInfoDto> getSimpleUsersOfTravel(Long travelId) {
        return travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ))
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllTravels() {
        travelRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<SimpleCostResponseDto> getCostsByTravelId(Long travelId) {
        List<Cost> costs = costRepository.findCostsByTravelId(travelId);
        List<User> usersByTravelId = userRepository.findUsersByTravelId(travelId);
        List<SimpleCostResponseDto> costsResponseDtos = new ArrayList<>();

        return costs.stream().map(SimpleCostResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SimpleScheduleResponseDto> getSchedulesByTravelId(Long travelId) {
        return travelRepository
                .findSchedulesWithPlaceByTravelId(travelId)
                .stream()
                .map(SimpleScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeScheduleOrder(Long travelId,
                                    ScheduleOrderUpdateRequestDto requestDto) {
        Travel travel = travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        travel.setScheduleOrder(requestDto.getScheduleOrder());
    }

    @Transactional
    public void rejectInvitation(Long userId, UUID code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 User가 존재하지 않습니다."
                        ,ErrorCode.USER_NOT_FOUND
                ));
        List<Invitation> all = invitationRepository.findAll();
        Invitation invitation = invitationRepository.findByCodeAndEmail(code, user.getEmail())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 초대가 존재하지 않습니다.",
                        ErrorCode.INVALID_INVITATION
                ));
        invitationRepository.delete(invitation);
    }
}
