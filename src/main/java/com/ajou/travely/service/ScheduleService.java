package com.ajou.travely.service;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.repository.PlaceRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.TravelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final PlaceRepository placeRepository;

    private final TravelRepository travelRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PlaceRepository placeRepository, TravelRepository travelRepository) {
        this.scheduleRepository = scheduleRepository;
        this.placeRepository = placeRepository;
        this.travelRepository = travelRepository;
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
    public Schedule createSchedule(Long travelId, Long placeId, LocalDateTime startTime, LocalDateTime endTime) {
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new RuntimeException("해당 travel이 존재하지 않습니다."));
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new RuntimeException("해당 place가 존재하지 않습니다."));
        return scheduleRepository.save(Schedule.builder().travel(travel).place(place).startTime(startTime).endTime(endTime).build());
    }

    @Transactional
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public Schedule getScheduleWithPlaceById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findScheduleByIdWithPlace(scheduleId).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        return schedule;
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
    public void deleteAllSchedules() {
        scheduleRepository.deleteAll();
    }
}
