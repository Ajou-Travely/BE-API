package com.ajou.travely.controller.travel;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.CostUpdateDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.service.CostService;
import com.ajou.travely.service.ScheduleService;
import com.ajou.travely.service.TravelService;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/travels")
@RequiredArgsConstructor
@RestController
public class TravelController {

    private final TravelService travelService;

    private final ScheduleService scheduleService;

    private final CostService costService;

    private final UserService userService;

    //Travels

    @GetMapping("")
    public List<SimpleTravelResponseDto> showAllTravels() {
        return travelService.getAllTravels();
    }

    @GetMapping("/travels")
    public ResponseEntity<Page<SimpleTravelResponseDto>> showTravelsByUser(
            @LoginUser SessionUser sessionUser,
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SimpleTravelResponseDto> travels =  userService.getTravelsByUser(sessionUser.getUserId(), pageable);
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getTravelById(travelId));
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

    @PatchMapping("/{travelId}")
    public ResponseEntity<Void> updateTravel(@PathVariable Long travelId,
                                             @RequestBody TravelUpdateRequestDto requestDto) {
        travelService.updateTravel(travelId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{travelId}/invite")
    public ResponseEntity<Void> inviteUserToTravel(@PathVariable Long travelId
            , @RequestBody TravelInviteRequestDto travelInviteRequestDto) {
        travelService.inviteUserToTravel(travelId, travelInviteRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{travelId}/accept/{code}")
    public void acceptInvitation(
            @LoginUser SessionUser sessionUser,
            @PathVariable Long travelId,
            @PathVariable UUID code,
            HttpServletResponse response) throws IOException {
        String redirectUri = travelService.acceptInvitation(sessionUser.getUserId(), travelId, code);
        response.sendRedirect(redirectUri);
    }

    @GetMapping("/reject/{code}")
    public ResponseEntity<Void> rejectInvitation(@LoginUser SessionUser sessionUser,
                                                 @PathVariable UUID code) {
        travelService.rejectInvitation(sessionUser.getUserId(), code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{travelId}/users")
    public ResponseEntity<List<SimpleUserInfoDto>> showUsersByTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getSimpleUsersOfTravel(travelId));
    }

    // Schedules

    @GetMapping("/{travelId}/schedules")
    public ResponseEntity<List<SimpleScheduleResponseDto>> showSchedulesByTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getSchedulesByTravelId(travelId));
    }

    @PostMapping("/{travelId}/schedules")
    public ResponseEntity<Long> createSchedule(@PathVariable Long travelId,
                                               @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(travelId, scheduleCreateRequestDto));
    }

    @GetMapping("/{travelId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long travelId,
                                                               @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @PutMapping("/{travelId}/schedules/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Long travelId,
                                               @PathVariable Long scheduleId,
                                               @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto
    ) {
        scheduleService.updateSchedule(scheduleId, scheduleUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{travelId}/change")
    public ResponseEntity<Void> changeScheduleOrder(@PathVariable Long travelId,
                                                    ScheduleOrderUpdateRequestDto requestDto) {
        travelService.changeScheduleOrder(travelId, requestDto);
        return ResponseEntity.ok().build();
    }

    // Costs

    @GetMapping("/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> showCostsByTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId));
    }

    @GetMapping("/{travelId}/costs/{costId}")
    public CostResponseDto showCost(@PathVariable Long costId,
                                    @PathVariable Long travelId) {
        return this.costService.getCostById(costId);
    }

    @PostMapping("/{travelId}/costs")
    public CostCreateResponseDto createCost(@Valid @RequestBody CostCreateRequestDto costCreateRequestDto,
                                            @PathVariable Long travelId) {
        return costService.createCost(
                costCreateRequestDto.getTotalAmount(),
                costCreateRequestDto.getTravelId(),
                costCreateRequestDto.getTitle(),
                costCreateRequestDto.getContent(),
                costCreateRequestDto.getIsEquallyDivided(),
                costCreateRequestDto.getAmountsPerUser(),
                costCreateRequestDto.getPayerId()
        );
    }

    @PutMapping("/{travelId}/costs/{costId}")
    public ResponseEntity<Void> updateCost(@PathVariable Long travelId,
                                           @PathVariable Long costId,
                                           CostUpdateDto costUpdateDto) {
        this.costService.updateCostById(costId, costUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{travelId}/costs/{costId}")
    public ResponseEntity<Void> deleteCost(@PathVariable Long travelId,
                                           @PathVariable Long costId) {
        this.costService.deleteCostById(costId);
        return ResponseEntity.ok().build();
    }
}
