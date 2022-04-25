package com.ajou.travely.service;

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

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    private final UserRepository userRepository;

    private final UserTravelRepository userTravelRepository;

    @Transactional
    public TravelResponseDto createTravel(String title, LocalDate startDate, LocalDate endDate, Long userId) {
        Travel travel = Travel.noMemoBuilder().title(title).startDate(startDate).endDate(endDate).build();
        travelRepository.save(travel);
        Optional<User> user = userRepository.findById(userId);
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
        List<TravelResponseDto> travels = new ArrayList<>();
        travelRepository.findAll().forEach(t -> travels.add(new TravelResponseDto(t)));
        return travels;
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
        Travel travel = getTravelById(travelId);
        List<User> users = new ArrayList<>();
        travel.getUserTravels().forEach(ut -> users.add(ut.getUser()));
        return users;
    }

    @Transactional
    public List<SimpleUserInfoDto> getSimpleUsersOfTravel(Long travelId) {
        Travel travel = getTravelById(travelId);
        List<SimpleUserInfoDto> users = new ArrayList<>();
        travel.getUserTravels().forEach(ut -> {
            User user = ut.getUser();
            users.add(new SimpleUserInfoDto(user.getId(), user.getName()));
        });
        return users;
    }
}
