package com.ajou.travely.controller.travel;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.CostUpdateDto;
import com.ajou.travely.controller.material.dto.MaterialCreateOrUpdateRequestDto;
import com.ajou.travely.controller.material.dto.MaterialResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.service.CostService;
import com.ajou.travely.service.MaterialService;
import com.ajou.travely.service.ScheduleService;
import com.ajou.travely.service.TravelService;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    private final MaterialService materialService;

    @GetMapping()
    public ResponseEntity<Page<SimpleTravelResponseDto>> showTravelsByUser(
            @LoginUser SessionUser sessionUser,
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SimpleTravelResponseDto> travels = userService.getTravelsByUser(sessionUser.getUserId(), pageable);
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravel(@PathVariable Long travelId,
                                                        @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getTravelById(travelId, sessionUser.getUserId()));
    }

    @PostMapping()
    public ResponseEntity<Long> createTravel(@LoginUser SessionUser sessionUser,
                                             @Valid @RequestBody TravelCreateRequestDto requestDto) {
        System.out.println("sessionUser.getUserId() = " + sessionUser.getUserId());
        Travel travel = travelService.createTravel(
            sessionUser.getUserId(),
            requestDto
        );
        travelService.inviteUserToTravelWithNoValidation(travel, requestDto.getUserEmails());
        return ResponseEntity.ok(travel.getId());
    }

    @PatchMapping("/{travelId}")
    public ResponseEntity<Void> updateTravel(@PathVariable Long travelId,
                                             @LoginUser SessionUser sessionUser,
                                             @RequestBody TravelUpdateRequestDto requestDto) {
        travelService.updateTravel(travelId, sessionUser.getUserId(), requestDto);
        return ResponseEntity.ok().build();
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

    @PostMapping("/{travelId}/invite")
    public ResponseEntity<Void> inviteUserToTravel(@PathVariable Long travelId,
                                                   @LoginUser SessionUser sessionUser,
                                                   @RequestBody TravelInviteRequestDto travelInviteRequestDto) {
        travelService.inviteUserToTravel(travelId, sessionUser.getUserId(), travelInviteRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accept/{code}")
    public void acceptInvitation(
            @LoginUser SessionUser sessionUser,
            @PathVariable UUID code,
            HttpServletResponse response) throws IOException {
        String redirectUri = travelService.acceptInvitation(sessionUser.getUserId(), code);
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
        return ResponseEntity.ok(travelService.getSimpleUsersInfoOfTravel(travelId));
    }

    // Schedules

    @GetMapping("/{travelId}/schedules")
    public ResponseEntity<List<SimpleScheduleResponseDto>> showSchedulesByTravel(@PathVariable Long travelId,
                                                                                 @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getSchedulesByTravelId(travelId, sessionUser.getUserId()));
    }

    @GetMapping("/{travelId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> showSchedule(@PathVariable Long travelId,
                                                               @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @PostMapping("/{travelId}/schedules")
    public ResponseEntity<Long> createSchedule(@PathVariable Long travelId,
                                               @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(travelId, scheduleCreateRequestDto));
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
                                                    @LoginUser SessionUser sessionUser,
                                                    ScheduleOrderUpdateRequestDto requestDto) {
        travelService.changeScheduleOrder(travelId, sessionUser.getUserId(), requestDto);
        return ResponseEntity.ok().build();
    }

    // Costs

    @GetMapping("/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> showCostsByTravel(@PathVariable Long travelId,
                                                                         @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId, sessionUser.getUserId()));
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
                costCreateRequestDto, travelId
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

    // TODO 준비물: checked, title, 한 명 지정

    @GetMapping("/{travelId}/materials")
    public ResponseEntity<List<MaterialResponseDto>> getMaterials(@PathVariable Long travelId) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{travelId}/materials")
    public ResponseEntity<MaterialResponseDto> createMaterial(
        @PathVariable Long travelId,
        @RequestBody MaterialCreateOrUpdateRequestDto requestDto
    ) {
        MaterialResponseDto responseDto = materialService.createMaterial(travelId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{travelId}/materials/{materialId}")
    public ResponseEntity<MaterialResponseDto> updateMaterial(
        @PathVariable Long travelId,
        @PathVariable Long materialId,
        @RequestBody MaterialCreateOrUpdateRequestDto requestDto
    ) {
        MaterialResponseDto responseDto = materialService.updateMaterial(materialId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{travelId}/materials/{materialId}")
    public ResponseEntity<Long> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.ok(materialId);
    }

}
