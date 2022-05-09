package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.CostsResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
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
    public Travel insertTravel(Travel travel) {
        Optional<User> user = userRepository.findById(travel.getManagerId());
        if (user.isEmpty()) {
            throw new RuntimeException("유저 없음 ㅋㅋ");
        }
        travelRepository.save(travel);
        UserTravel userTravel = UserTravel.builder().user(user.get()).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
        return travel;
    }

    @Transactional
    public List<Travel> getAllTravels() {
        return travelRepository
                .findAll();
    }

    @Transactional
    public void addUserToTravel(Long travelId, Long userId) {
        Travel travel = getTravelById(travelId);
        Optional<User> user = userRepository.findById(userId);
        UserTravel userTravel = UserTravel.builder().user(user.get()).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
    }

    @Transactional
    public Travel getTravelById(Long travelId) {
        Optional<Travel> travel = travelRepository.findById(travelId);
        if (travel.isEmpty()) {
            throw new RuntimeException("그런 여행 없슴 ㅋㅋ;");
        }
        return travel.get();
    }

    @Transactional
    public List<User> getUsersOfTravel(Long travelId) {
        return getTravelById(travelId)
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SimpleUserInfoDto> getSimpleUsersOfTravel(Long travelId) {
        return getTravelById(travelId)
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
    public List<CostsResponseDto> getCostsByTravelId(Long travelId) {
        List<Cost> costs = costRepository.findCostsByTravelId(travelId);
        List<User> usersByTravelId = userRepository.findUsersByTravelId(travelId);
        List<CostsResponseDto> costsResponseDtos = new ArrayList<>();

        return costs.stream().map(CostsResponseDto::new).collect(Collectors.toList());
    }
}
