package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
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
    public Schedule insertSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Long createSchedule(Long travelId, ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new RuntimeException("해당 travel이 존재하지 않습니다."));
        Place place = createOrFindPlace(scheduleCreateRequestDto.getPlace());
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
                    () -> new RuntimeException("해당 user가 존재하지 않습니다.")
            );
            schedule.addUser(branchRepository.save(new Branch(user, schedule)));
        });
        return schedule.getId();
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 schedule이 존재하지 않습니다."));
        Map<Long, User> currentUsers = new HashMap<>();
        schedule
                .getTravel()
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
            User user = Optional.ofNullable(currentUsers.get(id)).orElseThrow(
                    () -> new RuntimeException("해당 user가 유효하지 않습니다.")
            );
            branchRepository.save(new Branch(user,schedule));
        });
        outUserIds.forEach(id -> {
            if (!currentUsers.containsKey(id)) {
                throw new RuntimeException("해당 user가 유효하지 않습니다.");
            }
            Branch branch = branchRepository.findByScheduleIdAndUserId(schedule.getId(), id)
                            .orElseThrow(() -> new RuntimeException("해당 branch가 존재하지 않습니다."));
            schedule.removeUser(branch);
            branchRepository.delete(branch);
        });
    }

    @Transactional
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findScheduleWithPlaceByScheduleId(scheduleId)
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }

    private Place createOrFindPlace(PlaceCreateRequestDto request) {
        return placeRepository.findByKakaoMapId(request.getKakaoMapId())
                .orElse(placeRepository.save(Place
                        .builder()
                        .kakaoMapId(request.getKakaoMapId())
                        .placeUrl(request.getPlaceUrl())
                        .placeName(request.getPlaceName())
                        .addressRoadName(request.getAddressRoadName())
                        .addressName(request.getAddressName())
                        .x(request.getX())
                        .y(request.getY())
                        .build()));
    }
}
