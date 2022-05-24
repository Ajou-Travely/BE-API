package com.ajou.travely.controller.travel;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.service.ScheduleService;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/travels")
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
        System.out.println("sessionUser.getUserId() = " + sessionUser.getUserId());
        Travel travel = travelService.createTravel(
                sessionUser.getUserId()
                , travelCreateRequestDto
        );
        travelCreateRequestDto.getUserEmails().forEach(
                email -> travelService.inviteUserToTravelWithNoValidation(travel, email)
        );
        return ResponseEntity.ok(travel.getId());
    }


    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravelById(@PathVariable Long travelId,
                                                            @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getTravelById(travelId, sessionUser.getUserId()));
    }

    @PostMapping("/accept/{code}")
    public ResponseEntity<Long> addUserToTravel(@LoginUser SessionUser sessionUser,
                                                @PathVariable UUID code) {
        return ResponseEntity
                .ok(
                        travelService
                                .addUserToTravelWithValidation(sessionUser.getUserId()
                                        , code));
    }

    @GetMapping("/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> showCostsByTravelId(@PathVariable Long travelId,
                                                                           @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId, sessionUser.getUserId()));
    }

    @PostMapping("/{travelId}/schedules")
    public ResponseEntity<Long> createSchedule(@PathVariable Long travelId,
                                               @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(travelId, scheduleCreateRequestDto));
    }

    @GetMapping("/{travelId}/schedules")
    public ResponseEntity<List<SimpleScheduleResponseDto>> showSchedulesByTravelId(@PathVariable Long travelId,
                                                                                   @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getSchedulesByTravelId(travelId, sessionUser.getUserId()));
    }

    @PostMapping("/{travelId}/invite")
    public ResponseEntity<Void> inviteUserToTravel(@PathVariable Long travelId,
                                                   @LoginUser SessionUser sessionUser,
                                                   @RequestBody TravelInviteRequestDto travelInviteRequestDto) {
        travelService.inviteUserToTravel(travelId, sessionUser.getUserId(), travelInviteRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{travelId}/change")
    public ResponseEntity<Void> changeScheduleOrder(@PathVariable Long travelId,
                                                    @LoginUser SessionUser sessionUser,
                                                    ScheduleOrderUpdateRequestDto requestDto) {
        travelService.changeScheduleOrder(travelId, sessionUser.getUserId(), requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{travelId}")
    public ResponseEntity<Void> updateTravel(@PathVariable Long travelId ,
                                             @LoginUser SessionUser sessionUser,
                                             @RequestBody TravelUpdateRequestDto requestDto) {
        travelService.updateTravel(travelId, sessionUser.getUserId(), requestDto);
        return ResponseEntity.ok().build();
    }
}
