package com.ajou.travely.controller.travel;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelInviteRequestDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.service.ScheduleService;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/travels")
@RequiredArgsConstructor
@RestController
public class TravelController {
    private final TravelService travelService;
    private final ScheduleService scheduleService;

    @GetMapping("")
    public List<SimpleTravelResponseDto> showAllTravels() {
        return travelService.getAllTravels();
    }

    @PostMapping("")
    public ResponseEntity<Long> createTravel(@LoginUser SessionUser sessionUser,
                                             @Valid @RequestBody TravelCreateRequestDto travelCreateRequestDto) {
        Travel travel = travelService.createTravel(
                sessionUser.getUserId()
                , travelCreateRequestDto
        );
        travelCreateRequestDto.getUserEmails().forEach(
                email -> travelService.inviteUserToTravelWithNoValidation(travel, email)
        );
        return ResponseEntity.ok(travel.getId());
    }

    @PostMapping("/accept/{code}")
    public ResponseEntity<Long> addUserToTravel(@LoginUser SessionUser sessionUser, @PathVariable UUID code) {
        return ResponseEntity
                .ok(
                        travelService
                                .addUserToTravelWithValidation(sessionUser.getUserId()
                                        , code));
    }

    @GetMapping("/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> showCostsByTravelId(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId));
    }

    @PostMapping("/{travelId}/schedules")
    public ResponseEntity<Long> createSchedule(@PathVariable Long travelId, @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(travelId, scheduleCreateRequestDto));
    }

    @GetMapping("/{travelId}/schedules")
    public ResponseEntity<List<SimpleScheduleResponseDto>> showSchedulesByTravelId(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getSchedulesByTravelId(travelId));
    }

    @PostMapping("/{travelId}/invite")
    public ResponseEntity<Void> inviteUserToTravel(@PathVariable Long travelId
            , @RequestBody TravelInviteRequestDto travelInviteRequestDto) {
        travelService.inviteUserToTravel(travelId, travelInviteRequestDto);
        return ResponseEntity.ok().build();
    }
}
