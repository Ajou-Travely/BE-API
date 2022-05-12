package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import com.ajou.travely.repository.UserTravelRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Long createTravel(Long userId, TravelCreateRequestDto travelCreateRequestDto) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 id의 유저가 존재하지 않습니다."));
        Travel travel = travelRepository.save(
                Travel.builder()
                        .title(travelCreateRequestDto.getTitle())
                        .startDate(travelCreateRequestDto.getStartDate())
                        .endDate(travelCreateRequestDto.getEndDate())
                        .managerId(travelCreateRequestDto.getUserId())
                        .build());
        UserTravel userTravel = UserTravel.builder().user(user).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
        return travel.getId();
    }

    @Transactional
    public List<SimpleTravelResponseDto> getAllTravels() {
        return travelRepository.
                findAllTravelsWithUser().
                stream().
                map(SimpleTravelResponseDto::new).
                collect(Collectors.toList());
    }

    @Transactional
    public void addUserToTravel(Long travelId, Long userId) {
        Travel travel = travelRepository
                .findTravelWithUsersById(travelId)
                .orElseThrow(() -> new RuntimeException("해당 id의 여행이 존재하지 않습니다."));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 id의 유저가 존재하지 않습니다."));
        UserTravel userTravel = UserTravel.builder().user(user).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
    }

    @Transactional
    public TravelResponseDto getTravelById(Long travelId) {
        Travel travel = travelRepository
                .findTravelWithUsersById(travelId)
                .orElseThrow(() -> new RuntimeException("해당 id의 여행이 존재하지 않습니다."));
        List<Schedule> schedules = travelRepository.findSchedulesByTravelId(travel.getId());
        return new TravelResponseDto(travel, schedules);
    }

    @Transactional
    public List<User> getUsersOfTravel(Long travelId) {
        return travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RuntimeException("해당 id의 여행이 존재하지 않습니다."))
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SimpleUserInfoDto> getSimpleUsersOfTravel(Long travelId) {
        return travelRepository
                .findById(travelId)
                .orElseThrow(() -> new RuntimeException("해당 id의 여행이 존재하지 않습니다."))
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
}
