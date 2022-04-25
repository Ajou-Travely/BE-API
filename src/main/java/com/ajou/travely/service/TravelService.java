package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.travel.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.travel.TravelResponseDto;
import com.ajou.travely.controller.travel.dto.user.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import com.ajou.travely.repository.UserTravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    private final UserRepository userRepository;

    private final UserTravelRepository userTravelRepository;

    @Transactional
    public TravelResponseDto createTravel(TravelCreateRequestDto travelCreateRequestDto) {
        Travel travel = travelCreateRequestDto.toEntity();
        travelRepository.save(travel);
        Optional<User> user = userRepository.findById(travelCreateRequestDto.getUserId());
        if (user.isEmpty()) {
            throw new RuntimeException("유저 없음 ㅋㅋ");
        }
        UserTravel userTravel = UserTravel.builder().user(user.get()).travel(travel).build();
        userTravelRepository.save(userTravel);
        travel.addUserTravel(userTravel);
        travelRepository.save(travel);
        return new TravelResponseDto(travel);
    }

    @Transactional
    public List<TravelResponseDto> getAllTravels() {
        return travelRepository
                .findAll()
                .stream()
                .map(TravelResponseDto::new)
                .collect(Collectors.toList());
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
                .map(u -> new SimpleUserInfoDto(u.getId(), u.getName()))
                .collect(Collectors.toList());
    }
}
