package com.ajou.travely.service;

import com.ajou.travely.controller.scheduler.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.scheduler.dto.ScheduleResponseDto;
import com.ajou.travely.controller.scheduler.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.User;
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
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Schedule insertSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Travel travel = travelRepository.findById(scheduleCreateRequestDto.getTravelId())
                .orElseThrow(() -> new RuntimeException("해당 travel이 존재하지 않습니다."));
        Place place = placeRepository.findById(scheduleCreateRequestDto.getPlaceId())
                .orElseThrow(() -> new RuntimeException("해당 place가 존재하지 않습니다."));
        Schedule schedule = scheduleRepository.save(
                Schedule.builder()
                        .travel(travel)
                        .place(place)
                        .startTime(scheduleCreateRequestDto.getStartTime())
                        .endTime(scheduleCreateRequestDto.getEndTime())
                        .build()
        );
        scheduleCreateRequestDto.getUserIds().forEach(i -> {
            User user = userRepository.findById(i).orElseThrow(
                    () -> new RuntimeException("해당 user가 존재하지 않습니다.")
            );
            schedule.addUser(branchRepository.save(new Branch(user, schedule)));
        });
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequestDto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("해당 schedule이 존재하지 않습니다."));
        Map<Long, User> currentUsers = new HashMap<>();
        schedule.getTravel().getUserTravels().forEach(ut -> currentUsers.put(ut.getUser().getId(), ut.getUser()));
        if (!Objects.equals(schedule.getPlace().getId(), scheduleUpdateRequestDto.getPlaceId())) {
            Place place = placeRepository.findById(scheduleUpdateRequestDto.getPlaceId())
                    .orElseThrow(() -> new RuntimeException("해당 place가 존재하지 않습니다."));
            schedule.setPlace(place);
        }
        if (schedule.getStartTime() != scheduleUpdateRequestDto.getStartTime()) {
            schedule.setStartTime(scheduleUpdateRequestDto.getStartTime());
        }
        if (schedule.getEndTime() != scheduleUpdateRequestDto.getEndTime()) {
            schedule.setEndTime(scheduleUpdateRequestDto.getEndTime());
        }
        List<Long> outUserIds = schedule.getBranches().stream().map(b -> b.getUser().getId()).collect(Collectors.toList());
        List<Long> inUserIds = scheduleUpdateRequestDto.getUserIds();
        inUserIds.removeAll(outUserIds);
        outUserIds.removeAll(inUserIds);
        inUserIds.forEach(i -> {
            User user = Optional.ofNullable(currentUsers.get(i)).orElseThrow(
                    () -> new RuntimeException("해당 user가 유효하지 않습니다.")
            );
            schedule.addUser(branchRepository.save(new Branch(user,schedule)));
        });
        outUserIds.forEach(i -> {
            if (!currentUsers.containsKey(i)) {
                throw new RuntimeException("해당 user가 유효하지 않습니다.");
            }
            Branch branch = branchRepository.findByScheduleIdAndUserId(schedule.getId(), i)
                            .orElseThrow(() -> new RuntimeException("해당 branch가 존재하지 않습니다."));
            schedule.removeUser(branch);
            branchRepository.delete(branch);
        });
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public Schedule getScheduleWithPlaceById(Long scheduleId) {
        return scheduleRepository.findScheduleByIdWithPlace(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public List<Schedule> getSchedulesByPlaceId(Long placeId) {
        return scheduleRepository.findSchedulesByPlaceId(placeId);
    }

    @Transactional
    public List<Schedule> getSchedulesByPlaceName(String placeName) {
        return scheduleRepository.findSchedulesByPlaceName(placeName);
    }

    @Transactional
    public List<Schedule> getSchedulesByTravelId(Long travelId) {
        return scheduleRepository.findSchedulesByTravelId(travelId);
    }

    @Transactional
    public List<Schedule> getSchedulesWithPlaceByTravelId(Long travelId) {
        return scheduleRepository.findSchedulesWithPlaceByTravelId(travelId);
    }

    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }
}
