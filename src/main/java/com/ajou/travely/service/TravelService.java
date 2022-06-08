package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.controller.user.dto.UserResponseDto;
import com.ajou.travely.domain.cost.Cost;
import com.ajou.travely.domain.Invitation;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.cost.TravelTransaction;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.travel.TravelType;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.exception.custom.UnauthorizedException;
import com.ajou.travely.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
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

    private final TravelTransactionRepository travelTransactionRepository;

    private final CustomMailSender customMailSender;

    @PersistenceContext
    private final EntityManager em;

    @Value("${domain.base-url}/")
    private String baseUrl;

    @Transactional
    public Travel insertTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    @Transactional
    public TravelResponseDto createTravel(Long userId, TravelCreateRequestDto requestDto) {
        User user = checkUserRecord(userId);
        Travel travel = travelRepository.save(
            Travel.builder()
                .title(requestDto.getTitle())
                .managerId(userId)
                .travelType(requestDto.getTravelType())
                    .startDate(requestDto.getStartDate())
                    .endDate(requestDto.getEndDate())
                .build());
        UserTravel userTravel = UserTravel.builder()
                .user(user)
                .travel(travel)
                .build();
        userTravelRepository.save(userTravel);
        List<TravelDate> travelDates =  createTravelDates(travel, requestDto.getStartDate(), requestDto.getEndDate());
        addUserToTravelWithNoValidation(travel, requestDto.getUserEmails());
        return new TravelResponseDto(travel, travelDates, new ArrayList<>());
    }

    @Transactional
    public List<TravelDateResponseDto> updateTravelDates(Long userId, Long travelId, TravelDateUpdateRequestDto requestDto) {
        Travel travel = checkAuthorization(travelId, userId);
        travelDateRepository.deleteAllByTravel(travel);
        travel.updateDate(requestDto.getStartDate(), requestDto.getEndDate());
        travel.getTravelDates().clear();
        return createTravelDates(travel, requestDto.getStartDate(), requestDto.getEndDate())
                .stream().map(TravelDateResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public TravelDateResponseDto updateTravelDateTitle(Long userId, Long travelId, TravelDateTitleUpdateRequestDto requestDto) {
        Travel travel = checkAuthorization(travelId, userId);
        TravelDate travelDate = checkTravelDateRecord(travel.getId(), requestDto.getDate());
        travelDate.updateTitle(requestDto.getTitle());
        return new TravelDateResponseDto(travelDate);
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
    public Page<SimpleTravelResponseDto> getAllTravels(Pageable pageable) {
        return travelRepository
                .findAll(pageable)
                .map(SimpleTravelResponseDto::new);
    }

    @Transactional
    public void inviteUserToTravel(Long travelId, Long userId, TravelInviteRequestDto requestDto) {
        Travel travel = checkAuthorization(travelId, userId);
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

    private void addUserToTravelWithNoValidation(Travel travel, List<String> userEmails) {
        List<String> validEmails = userEmails.stream()
                .distinct()
                .collect(Collectors.toList());
        validEmails.forEach(email -> {
            Optional<User> isUser = userRepository.findByEmail(email);
            if (isUser.isPresent()) {
                User user = isUser.get();
                UserTravel userTravel = UserTravel.builder()
                        .user(user)
                        .travel(travel)
                        .build();
                userTravelRepository.save(userTravel);
            }
        });
    }

    @Transactional
    public void inviteUserToTravelWithNoValidation(Long travelId, List<String> userEmails) {
        Travel travel = checkTravelRecord(travelId);
        List<String> validEmails = userEmails.stream()
                .distinct()
                .collect(Collectors.toList());
        validEmails.forEach(email -> {
            Optional<User> isUser = userRepository.findByEmail(email);
            if (isUser.isEmpty()) {
                UUID code = UUID.randomUUID();
                String text = baseUrl + "invite/accept/" + code;
                customMailSender.sendInvitationEmail(email, text);
                invitationRepository.save(
                        new Invitation(email, travel, code)
                );
            }
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

    @Transactional
    public TravelResponseDto getTravelById(Long travelId, Long userId) {
        Travel travel = checkAuthorization(travelId, userId);
        List<CostResponseDto> costs = getCostsByTravelId(travelId);
        return new TravelResponseDto(travel, travel.getTravelDates(), costs);
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
    public List<CostResponseDto> getCostsByTravelId(Long travelId) {
        List<Cost> costs = costRepository.findCostsByTravelId(travelId);
        return costs.stream().map(CostResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SimpleScheduleResponseDto> getSchedulesByTravelIdAndDate(Long travelId, LocalDate date) {
        TravelDate travelDate = checkTravelDateRecord(travelId, date);
        return sortSchedule(travelDate)
            .stream()
            .map(SimpleScheduleResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<Long> changeScheduleOrder(Long travelId, LocalDate date, ScheduleOrderUpdateRequestDto requestDto) {
        TravelDate travelDate = checkTravelDateRecord(travelId, date);
        travelDate.setScheduleOrder(requestDto.getScheduleOrder());
        return travelDate.getScheduleOrder();
    }

    @Transactional
    public TravelUpdateResponseDto updateTravel(Long travelId,
                             Long userId,
                             TravelUpdateRequestDto requestDto) {
        Travel travel = checkAuthorization(travelId, userId);
        travel.updateTravel(requestDto);
        return new TravelUpdateResponseDto(travel);
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

    private List<User> getUsersOfTravel(Long travelId) {
        Travel travel = checkTravelRecord(travelId);
        return travel
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .collect(Collectors.toList());
    }

    private Travel checkAuthorization(Long travelId, Long userId) {
        Travel travel = checkTravelRecord(travelId);
        if (travel.getTravelType().equals(TravelType.PRIVATE) && userId != -1
                && !getUsersOfTravel(travelId).stream().map(User::getId).collect(Collectors.toList()).contains(userId)) {
            throw new UnauthorizedException("해당 Travel에 대해 접근 권한이 없습니다.", ErrorCode.UNAUTHORIZED_TRAVEL);
        }
        return travel;
    }

    private List<Schedule> sortSchedule(TravelDate travelDate) {
        Map<Long, Schedule> map = new HashMap<>();
        travelDateRepository
                .findSchedulesWithPlaceByDateAndTravelId(travelDate.getDate(), travelDate.getTravel().getId())
                .forEach(schedule -> map.put(schedule.getId(), schedule));
        List<Schedule> schedules = new ArrayList<>();
        travelDate.getScheduleOrder().forEach(id -> schedules.add(map.get(id)));
        return schedules;
    }
    /*------------------------------------------------------*/

//    public void createTravelDate(Long travelId, String title, LocalDate date) {
//        travelDateRepository.save(TravelDate.builder()
//                .title(title)
//                .travel(checkTravelRecord(travelId))
//                .date(date)
//                .build()
//        );
//    }

    @Transactional
    public void deleteTravelDate(Long travelId, LocalDate date) {
        travelDateRepository.delete(checkTravelDateRecord(travelId, date));
    }

    // TravelTransaction
    @Transactional
    public TravelTransactionCreateResponseDto createTravelTransaction(Long travelId,
                                                                      Long userId,
                                                                      TravelTransactionCreateRequestDto travelTransactionCreateRequestDto) {
        Travel travel = checkTravelRecord(travelId);
        User createdBy = checkUserRecord(userId);
        User sender = checkUserRecord(travelTransactionCreateRequestDto.getSenderId());
        User receiver = checkUserRecord(travelTransactionCreateRequestDto.getReceiverId());

        TravelTransaction travelTransaction = travelTransactionRepository.save(
                TravelTransaction.builder()
                        .travel(travel)
                        .sender(sender)
                        .receiver(receiver)
                        .createdBy(createdBy)
                        .build()
        );

        return new TravelTransactionCreateResponseDto(travelTransaction);
    }

    @Transactional(readOnly = true)
    public TravelTransactionResponseDto getAllTravelTransactionsByUserId(Long travelId,
                                                                        Long userId) {
        List<TravelTransaction> bySenderId = travelTransactionRepository.findBySenderId(userId);
        List<TravelTransaction> byReceiverId = travelTransactionRepository.findByReceiverId(userId);

        List<TravelTransactionResponseToSendDto> userInfoAndAmountToSend = new ArrayList<>();
        List<TravelTransactionResponseToReceiveDto> userInfoAndAmountToReceive = new ArrayList<>();

        for (TravelTransaction travelTransaction : bySenderId) {
            userInfoAndAmountToSend.add(TravelTransactionResponseToSendDto.builder()
                    .travelTransactionId(travelTransaction.getId())
                    .userToRecieve(new SimpleUserInfoDto(travelTransaction.getReceiver()))
                    .amount(travelTransaction.getAmount())
                    .build());
        }

        for (TravelTransaction travelTransaction : byReceiverId) {
            userInfoAndAmountToReceive.add(TravelTransactionResponseToReceiveDto.builder()
                    .travelTransactionId(travelTransaction.getId())
                    .userToSend(new SimpleUserInfoDto(travelTransaction.getSender()))
                    .amount(travelTransaction.getAmount())
                    .build());
        }

        return new TravelTransactionResponseDto(userInfoAndAmountToSend, userInfoAndAmountToReceive);
    }

    @Transactional
    public void updateTravelTransaction(Long travelTransactionId, Long userId, TravelTransactionUpdateDto travelTransactionUpdateDto) {
        TravelTransaction travelTransaction = checkTravelTransactionRecord(travelTransactionId);
        travelTransaction.updateTravelTransaction(
                checkUserRecord(travelTransactionUpdateDto.getSenderId()),
                checkUserRecord(travelTransactionUpdateDto.getReceiverId()),
                checkUserRecord(userId),
                travelTransactionUpdateDto.getAmount()
        );
    }

    @Transactional
    public void deleteTravelTransaction(Long travelTransactionId) {
        travelTransactionRepository.delete(checkTravelTransactionRecord(travelTransactionId));
    }

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

    private TravelDate checkTravelDateRecord(Long travelId, LocalDate date) {
        return checkRecord(
                travelDateRepository.findTravelDateByDateAndTravelId(date, travelId),
                "해당 여행과 날짜에 해당하는 TravelDate가 존재하지 않습니다.",
                ErrorCode.TRAVEL_DATE_NOT_FOUND
        );
    }

    private TravelTransaction checkTravelTransactionRecord(Long travelTransactionId) {
        return checkRecord(
                travelTransactionRepository.findById(travelTransactionId),
                "해당 ID의 Transaction을 찾을 수 없습니다.",
                ErrorCode.TRAVEL_TRANSACTION_NOT_FOUND
        );
    }

    private <T> T checkRecord(Optional<T> record, String message, ErrorCode code) {
        return record.orElseThrow(() ->
                new RecordNotFoundException(message, code));
    }

    private List<TravelDate> createTravelDates(Travel travel, LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        List<TravelDate> returnTravelDates = new ArrayList<>();
        int day = 1;
        while (endDate.compareTo(currentDate) >= 0) {
            TravelDate save = travelDateRepository.save(
                    TravelDate
                            .builder()
                            .date(currentDate)
                            .title("day" + day)
                            .travel(travel)
                            .build()
            );
            returnTravelDates.add(save);
            currentDate = currentDate.plusDays(1);
            day += 1;
        }
        return returnTravelDates;
    }
}
