package com.ajou.travely.service;

import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Travel travel = travelRepository.findById(scheduleCreateRequestDto.getTravelId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        Place place = placeRepository.findById(scheduleCreateRequestDto.getPlaceId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Travel이 존재하지 않습니다."
                        , ErrorCode.TRAVEL_NOT_FOUND
                ));
        Schedule schedule = scheduleRepository.save(
                Schedule.builder()
                        .travel(travel)
                        .place(place)
                        .startTime(scheduleCreateRequestDto.getStartTime())
                        .endTime(scheduleCreateRequestDto.getEndTime())
                        .build()
        );
        scheduleCreateRequestDto.getUserIds().forEach(id -> {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new RecordNotFoundException(
                            "해당 ID의 User가 존재하지 않습니다."
                            , ErrorCode.USER_NOT_FOUND
                    )
            );
            schedule.addUser(branchRepository.save(new Branch(user, schedule)));
        });
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequestDto.getScheduleId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Schedule이 존재하지 않습니다."
                        , ErrorCode.SCHEDULE_NOT_FOUND
                ));
        Map<Long, User> currentUsers = new HashMap<>();
        schedule.getTravel().getUserTravels().forEach(ut -> currentUsers.put(ut.getUser().getId(), ut.getUser()));
        if (!Objects.equals(schedule.getPlace().getId(), scheduleUpdateRequestDto.getPlaceId())) {
            Place place = placeRepository.findById(scheduleUpdateRequestDto.getPlaceId())
                    .orElseThrow(() -> new RecordNotFoundException(
                            "해당 ID의 Place가 존재하지 않습니다."
                            , ErrorCode.PLACE_NOT_FOUND
                    ));
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
            User user = Optional.ofNullable(currentUsers.get(id)).orElseThrow(
                    () -> new RecordNotFoundException(
                            "해당 ID의 User가 존재하지 않습니다."
                            , ErrorCode.USER_NOT_FOUND
                    )
            );
            branchRepository.save(new Branch(user,schedule));
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
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findScheduleWithPlaceByScheduleId(scheduleId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID의 Schedule이 존재하지 않습니다."
                        , ErrorCode.SCHEDULE_NOT_FOUND
                ));
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }
}
