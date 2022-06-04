package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.schedulePhoto.dto.SchedulePhotoResponseDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.SchedulePhoto;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final AwsS3Service s3Service;

    private final SchedulePhotoRepository schedulePhotoRepository;

    private final ScheduleRepository scheduleRepository;

    private final PlaceRepository placeRepository;

    private final TravelRepository travelRepository;

    private final BranchRepository branchRepository;

    private final UserRepository userRepository;

    private final TravelDateRepository travelDateRepository;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Schedule insertSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public SimpleScheduleResponseDto createSchedule(Long travelId, LocalDate date, ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Travel travel = checkTravelRecord(travelId);
        TravelDate travelDate = checkTravelDateRecord(travelId, date);
        Place place = createOrFindPlace(scheduleCreateRequestDto.getPlace());
        Schedule schedule = scheduleRepository.save(
                Schedule.builder()
                        .travelDate(travelDate)
                        .place(place)
                        .startTime(scheduleCreateRequestDto.getStartTime())
                        .endTime(scheduleCreateRequestDto.getEndTime())
                        .build()
        );
        travelDate.getScheduleOrder().add(schedule.getId());
        System.out.println(travelDate.getScheduleOrder().size());
        scheduleCreateRequestDto.getUserIds().forEach(id ->
                schedule.addUser(branchRepository.save(new Branch(checkUserRecord(id), schedule)))
        );

        return new SimpleScheduleResponseDto(schedule);
    }

    @Transactional
    public SimpleScheduleResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        Schedule schedule = checkScheduleRecord(scheduleId);
        Map<Long, User> currentUsers = new HashMap<>();
        schedule.getTravelDate()
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

        em.flush();
        em.clear();

        Schedule returnSchedule = checkScheduleRecord(scheduleId);
        return new SimpleScheduleResponseDto(returnSchedule);
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

    public List<String> uploadSchedulePhotos(Long userId, Long scheduleId, List<MultipartFile> photos) {
        User user = checkUserRecord(userId);
        Schedule schedule = checkScheduleRecord(scheduleId);
        List<String> returnPhotoPaths = new ArrayList<>();
        List<SchedulePhoto> schedulePhotos =
                s3Service.uploadFiles(photos).stream()
                        .map(photoPath -> {
                                    returnPhotoPaths.add(photoPath);
                                    return SchedulePhoto.builder()
                                            .user(user)
                                            .schedule(schedule)
                                            .photoPath(photoPath)
                                            .build();
                                }
                        )
                        .collect(Collectors.toList());
        schedulePhotoRepository.saveAll(schedulePhotos);
        schedule.addSchedulePhotos(schedulePhotos);

        return returnPhotoPaths;
    }

    // TODO: schedule에 user가 참가 중인지 checking
    public List<SchedulePhotoResponseDto> getSchedulePhotos(Long scheduleId) {
        Schedule schedule = checkScheduleRecord(scheduleId);
        return schedule.getPhotos().stream()
                .map(SchedulePhotoResponseDto::new)
                .collect(Collectors.toList());
    }

    public void deleteSchedulePhotos(Long scheduleId, List<Long> schedulePhotoIds) {
        Schedule schedule = checkScheduleRecord(scheduleId);
        List<SchedulePhoto> schedulePhotos =
            schedulePhotoRepository.findAllById(schedulePhotoIds);
        schedule.removeSchedulePhotos(schedulePhotos);
        schedulePhotoRepository.deleteAll(schedulePhotos);
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

    private TravelDate checkTravelDateRecord(Long travelId, LocalDate date) {
        return checkRecord(
                travelDateRepository.findTravelDateByDateAndTravelId(date, travelId),
                "해당 ID의 Date가 존재하지 않습니다.",
                ErrorCode.TRAVEL_DATE_NOT_FOUND
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
