package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.controller.user.dto.UserResponseDto;
import com.ajou.travely.domain.*;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
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

    private final TravelDateRepository travelDateRepository;

    private final CustomMailSender customMailSender;

    @Value("${domain.base-url}")
    private String baseUrl;

    @Transactional
    public Travel insertTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    @Transactional
    public Travel createTravel(Long userId, TravelCreateRequestDto travelCreateRequestDto) {
        User user = checkUserRecord(userId);
        Travel travel = travelRepository.save(
            Travel.builder()
                .title(travelCreateRequestDto.getTitle())
                .startDate(travelCreateRequestDto.getStartDate())
                .endDate(travelCreateRequestDto.getEndDate())
                .managerId(userId)
                .travelType(travelCreateRequestDto.getTravelType())
                .build());
        UserTravel userTravel = UserTravel.builder()
            .user(user)
            .travel(travel)
            .build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
        return travel;
    }

    @Transactional
    public List<SimpleTravelResponseDto> getAllTravels() {
        return travelRepository
            .findAll()
            .stream()
            .map(SimpleTravelResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void inviteUserToTravel(Long travelId, TravelInviteRequestDto requestDto) {
        Travel travel = checkTravelRecord(travelId);
        checkAlreadyInvitedUsers(travelId, requestDto.getEmail());
        checkAlreadyParticipatedUsers(travelId, requestDto.getEmail());

        UUID code = UUID.randomUUID();
        String text = baseUrl + "invite/accept/" + code;
        customMailSender.sendInvitationEmail(
            requestDto.getEmail(),
            text
        );
        invitationRepository.save(
            new Invitation(
                requestDto.getEmail(),
                travel,
                code
            )
        );

    }

    private void checkAlreadyInvitedUsers(Long travelId, String userEmail) {
        if (getUsersOfTravel(travelId).stream()
            .map(User::getEmail)
            .collect(Collectors.toList())
            .contains(userEmail)
        ) {
            throw new DuplicatedRequestException(
                "이미 여행에 초대된 사용자입니다.",
                ErrorCode.ALREADY_REQUESTED
            );
        }
    }

    private void checkAlreadyParticipatedUsers(Long travelId, String userEmail) {
        if (invitationRepository.findByTravelIdAndEmail(travelId, userEmail)
            .isPresent()) {
            throw new DuplicatedRequestException(
                "이미 여행에 참가 중인 사용자입니다.",
                ErrorCode.ALREADY_REQUESTED
            );
        }
    }

    @Transactional
    public void inviteUserToTravelWithNoValidation(Travel travel, List<String> userEmails) {
        List<String> validEmails = userEmails.stream()
            .distinct()
            .collect(Collectors.toList());
        validEmails.forEach(email -> {
            UUID code = UUID.randomUUID();
            String text = baseUrl + "invite/accept/" + code;
            customMailSender.sendInvitationEmail(email, text);
            invitationRepository.save(
                new Invitation(email, travel, code)
            );
        });
    }

    @Transactional
    public Long addUserToTravelWithValidation(Long userId, UUID code) {
        User user = checkUserRecord(userId);
        Invitation invitation = checkInvitationRecord(code, user.getEmail());
        Travel travel = checkTravelRecord(invitation.getTravel().getId());

        invitationRepository.deleteById(invitation.getId());
        UserTravel userTravel = UserTravel.builder()
            .user(user)
            .travel(travel)
            .build();
        userTravelRepository.save(userTravel);
        return travel.getId();
    }

    @Transactional
    public void addUserToTravel(Long travelId, Long userId) {
        UserTravel userTravel = UserTravel.builder()
            .user(checkUserRecord(userId))
            .travel(checkTravelRecord(travelId))
            .build();
        userTravelRepository.save(userTravel);
    }

    // 수정해야함.
    /*------------------------------------------------------*/
    @Transactional
    public TravelResponseDto getTravelById(Long travelId) {
//        Travel travel = checkTravelRecord(travelId);
//        List<Schedule> schedules = sortSchedule(travel);
//        return new TravelResponseDto(travel, schedules);
        Travel travel = checkTravelRecord(travelId);
        return new TravelResponseDto(travel, travel.getTravelDates());
    }
    /*------------------------------------------------------*/
    private List<User> getUsersOfTravel(Long travelId) {
        Travel travel = checkTravelRecord(travelId);
        return travel
            .getUserTravels()
            .stream()
            .map(UserTravel::getUser)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<UserResponseDto> getUsersInfoOfTravel(Long travelId) {
        return getUsersOfTravel(travelId)
            .stream()
            .map(UserResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<SimpleUserInfoDto> getSimpleUsersInfoOfTravel(Long travelId) {
        return getUsersOfTravel(travelId)
            .stream()
            .map(SimpleUserInfoDto::new)
            .collect(Collectors.toList());
    }

    // TODO: Cascade 고려
    @Transactional
    public void deleteAllTravels() {
        travelRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<SimpleCostResponseDto> getCostsByTravelId(Long travelId) {
        List<Cost> costs = costRepository.findCostsByTravelId(travelId);
        return costs
            .stream()
            .map(SimpleCostResponseDto::new)
            .collect(Collectors.toList());
    }

    // 수정 필요.
    /*------------------------------------------------------*/
//    @Transactional(readOnly = true)
//    public List<SimpleScheduleResponseDto> getSchedulesByTravelId(Long travelId) {
//        Travel travel = checkTravelRecord(travelId);
//        return sortSchedule(travel)
//            .stream()
//            .map(SimpleScheduleResponseDto::new)
//            .collect(Collectors.toList());
//    }
    /*------------------------------------------------------*/


    // 수정 필요
    /*------------------------------------------------------*/
    @Transactional
    public void changeScheduleOrder(Long travelDateId, ScheduleOrderUpdateRequestDto requestDto) {
        checkTravelDateRecord(travelDateId).setScheduleOrder(requestDto.getScheduleOrder());
    }
    /*------------------------------------------------------*/

    @Transactional
    public void updateTravel(Long travelId, TravelUpdateRequestDto requestDto) {
        checkTravelRecord(travelId).updateTravel(requestDto);
    }

    @Transactional
    public String acceptInvitation(Long userId, UUID code) {
        Long travelId = addUserToTravelWithValidation(userId, code);
        return baseUrl + travelId;
    }

    @Transactional
    public void rejectInvitation(Long userId, UUID code) {
        User user = checkUserRecord(userId);
        Invitation invitation = checkInvitationRecord(code, user.getEmail());
        invitationRepository.delete(invitation);
    }

    // 수정 필요.
    /*------------------------------------------------------*/
    private List<Schedule> sortSchedule(TravelDate travelDate) {
//        Map<Long, Schedule> map = new HashMap<>();
//        travelRepository
//                .findSchedulesWithPlaceByTravelId(travel.getId())
//                .forEach(schedule -> map.put(schedule.getId(), schedule));
//        List<Schedule> schedules = new ArrayList<>();
//        travel.getScheduleOrder().forEach(id -> schedules.add(map.get(id)));
//        return schedules;
        Map<Long, Schedule> map = new HashMap<>();
        travelDateRepository
                .findSchedulesWithPlaceByTravelDateId(travelDate.getId())
                .forEach(schedule -> map.put(schedule.getId(), schedule));
        List<Schedule> schedules = new ArrayList<>();
        travelDate.getScheduleOrder().forEach(id -> schedules.add(map.get(id)));
        return schedules;
    }
    /*------------------------------------------------------*/

    private Travel checkTravelRecord(Long travelId) {
        return checkRecord(
            travelRepository.findById(travelId),
            "해당 ID의 Travel이 존재하지 않습니다.",
            ErrorCode.TRAVEL_NOT_FOUND
            );
    }

    private User checkUserRecord(Long userId) {
        return checkRecord(
            userRepository.findById(userId),
            "해당 ID의 User가 존재하지 않습니다.",
            ErrorCode.USER_NOT_FOUND
        );
    }

    private Invitation checkInvitationRecord(UUID code, String email) {
        return checkRecord(
            invitationRepository.findByCodeAndEmail(code, email),
            "잘못된 초대 링크입니다.",
            ErrorCode.INVALID_INVITATION
        );
    }

    private TravelDate checkTravelDateRecord(Long travelDateId) {
        return checkRecord(
                travelDateRepository.findById(travelDateId),
                "해당 ID의 Date가 존재하지 않습니다.",
                ErrorCode.DATE_NOT_FOUND
        );
    }

    private <T> T checkRecord(Optional<T> record, String message, ErrorCode code) {
        return record.orElseThrow(() ->
            new RecordNotFoundException(message, code));
    }

}
