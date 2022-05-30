package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final PlaceRepository placeRepository;

    private final TravelRepository travelRepository;

    private final BranchRepository branchRepository;

    private final UserRepository userRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PlaceRepository placeRepository, TravelRepository travelRepository, BranchRepository branchRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.placeRepository = placeRepository;
        this.travelRepository = travelRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Schedule insertSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Long createSchedule(Long travelId, ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Travel travel = checkTravelRecord(travelId);
        Place place = createOrFindPlace(scheduleCreateRequestDto.getPlace());
        Schedule schedule = scheduleRepository.save(
                Schedule.builder()
                        .travel(travel)
                        .place(place)
                        .startTime(scheduleCreateRequestDto.getStartTime())
                        .endTime(scheduleCreateRequestDto.getEndTime())
                        .build()
        );
        travel.getScheduleOrder().add(schedule.getId());
        scheduleCreateRequestDto.getUserIds().forEach(id -> {
            User user = checkUserRecord(id);
            schedule.addUser(branchRepository.save(new Branch(user, schedule)));
        });
        return schedule.getId();
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = checkScheduleRecord(scheduleId);
        Map<Long, User> currentUsers = new HashMap<>();
        schedule.getTravel()
                .getUserTravels()
                .forEach(userTravel -> currentUsers.put(userTravel.getUser().getId(), userTravel.getUser()));
        if (!Objects.equals(schedule.getPlace().getKakaoMapId(), scheduleUpdateRequestDto.getPlace().getKakaoMapId())) {
            Place place = createOrFindPlace(scheduleUpdateRequestDto.getPlace());
            schedule.setPlace(place);
        }
        if (schedule.getStartTime() != scheduleUpdateRequestDto.getStartTime()) {
            schedule.setStartTime(scheduleUpdateRequestDto.getStartTime());
        }
        if (schedule.getEndTime() != scheduleUpdateRequestDto.getEndTime()) {
            schedule.setEndTime(scheduleUpdateRequestDto.getEndTime());
        }
        List<Long> outUserIds = schedule
                .getBranches()
                .stream()
                .map(branch -> branch.getUser().getId())
                .collect(Collectors.toList());
        List<Long> inUserIds = scheduleUpdateRequestDto.getUserIds();
        inUserIds.removeAll(outUserIds);
        outUserIds.removeAll(inUserIds);
        inUserIds.forEach(id -> {
            User user = checkUserRecord(id);
            branchRepository.save(new Branch(user, schedule));
        });
        outUserIds.forEach(id -> {
            if (!currentUsers.containsKey(id)) {
                throw new RecordNotFoundException(
                        "해당 ID의 User가 존재하지 않습니다."
                        , ErrorCode.USER_NOT_FOUND
                );
            }
            Branch branch = branchRepository.findByScheduleIdAndUserId(schedule.getId(), id)
                    .orElseThrow(() -> new RecordNotFoundException(
                            "해당 ID의 Branch가 존재하지 않습니다."
                            , ErrorCode.BRANCH_NOT_FOUND
                    ));
            schedule.removeUser(branch);
            branchRepository.delete(branch);
        });
    }


    @Transactional
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = checkScheduleRecord(scheduleId);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }

    public void uploadSchedulePhotos(Long userId, Long scheduleId, List<MultipartFile> photos) {
        User user = checkUserRecord(userId);
        Schedule schedule = checkScheduleRecord(scheduleId);

    }

    private Travel checkTravelRecord(Long travelId) {
        return checkRecord(
                travelRepository.findById(travelId),
                "해당 ID의 Travel이 존재하지 않습니다.",
                ErrorCode.TRAVEL_NOT_FOUND
        );
    }

    private Schedule checkScheduleRecord(Long scheduleId) {
        return checkRecord(
                scheduleRepository.findById(scheduleId),
                "해당 ID의 Schedule이 존재하지 않습니다.",
                ErrorCode.SCHEDULE_NOT_FOUND
        );
    }

    private User checkUserRecord(Long userId) {
        return checkRecord(
                userRepository.findById(userId),
                "해당 ID의 User가 존재하지 않습니다.",
                ErrorCode.USER_NOT_FOUND
        );
    }

    private <T> T checkRecord(Optional<T> record, String message, ErrorCode code) {
        return record.orElseThrow(() ->
                new RecordNotFoundException(message, code));
    }

    private Place createOrFindPlace(PlaceCreateRequestDto request) {
        return placeRepository.findByKakaoMapId(request.getKakaoMapId())
                .orElseGet(() -> placeRepository.save(Place
                        .builder()
                        .kakaoMapId(request.getKakaoMapId())
                        .placeUrl(request.getPlaceUrl())
                        .placeName(request.getPlaceName())
                        .addressRoadName(request.getAddressRoadName())
                        .addressName(request.getAddressName())
                        .lat(request.getLat())
                        .lng(request.getLng())
                        .build()));
    }
}
