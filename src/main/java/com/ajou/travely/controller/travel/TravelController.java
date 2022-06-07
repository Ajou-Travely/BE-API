package com.ajou.travely.controller.travel;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.cost.dto.*;
import com.ajou.travely.controller.material.dto.MaterialCreateRequestDto;
import com.ajou.travely.controller.material.dto.MaterialResponseDto;
import com.ajou.travely.controller.material.dto.MaterialUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.schedulePhoto.dto.SchedulePhotoResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.kakao.KakaoMessageResponse;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.KakaoNotAuthenticationExcpetion;
import com.ajou.travely.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
        Page<SimpleTravelResponseDto> travels = userService.getTravelsByUser(
            sessionUser.getUserId(), pageable);
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravel(@PathVariable Long travelId,
        @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.getTravelById(travelId, sessionUser.getUserId()));
    }

    @PostMapping()
    public ResponseEntity<TravelResponseDto> createTravel(@LoginUser SessionUser sessionUser,
                                             @Valid @RequestBody TravelCreateRequestDto requestDto) {
        TravelResponseDto responseDto = travelService.createTravel(
            sessionUser.getUserId(),
            requestDto
        );
        travelService.inviteUserToTravelWithNoValidation(responseDto.getId(), requestDto.getUserEmails());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{travelId}")
    public ResponseEntity<TravelUpdateResponseDto> updateTravel(@PathVariable Long travelId,
        @LoginUser SessionUser sessionUser,
        @RequestBody TravelUpdateRequestDto requestDto) {
        return ResponseEntity.ok(travelService.updateTravel(travelId, sessionUser.getUserId(), requestDto));
    }

    @PostMapping("/accept/{code}")
    public ResponseEntity<Long> addUserToTravel(@LoginUser SessionUser sessionUser,
                                                @PathVariable UUID code) {
        Long travelId = travelService
            .addUserToTravelWithValidation(sessionUser.getUserId(), code);
        return ResponseEntity.ok(travelId);
    }

    @PostMapping("/{travelId}/invite")
    public ResponseEntity<Void> inviteUserToTravel(@PathVariable Long travelId,
        @LoginUser SessionUser sessionUser,
        @RequestBody TravelInviteRequestDto travelInviteRequestDto) {
        travelService.inviteUserToTravel(travelId, sessionUser.getUserId(), travelInviteRequestDto);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/accept/{code}")
//    public void acceptInvitation(
//        @LoginUser SessionUser sessionUser,
//        @PathVariable UUID code,
//        HttpServletResponse response) throws IOException {
//        String redirectUri = travelService.acceptInvitation(sessionUser.getUserId(), code);
//        response.sendRedirect(redirectUri);
//    }

    @DeleteMapping("/reject/{code}")
    public ResponseEntity<Void> rejectInvitation(@LoginUser SessionUser sessionUser,
        @PathVariable UUID code) {
        travelService.rejectInvitation(sessionUser.getUserId(), code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{travelId}/users")
    public ResponseEntity<List<SimpleUserInfoDto>> showUsersByTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getSimpleUsersInfoOfTravel(travelId));
    }

    // Travel Date

    @PutMapping("/{travelId}/dates")
    public ResponseEntity<List<TravelDateResponseDto>> updateTravelDates(@PathVariable Long travelId,
                                                              @RequestBody TravelDateUpdateRequestDto requestDto,
                                                              @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(travelService.updateTravelDates(sessionUser.getUserId(), travelId, requestDto));
    }

    @PatchMapping("/{travelId}/dates")
    public ResponseEntity<TravelDateResponseDto> updateTravelDateTitle(@PathVariable Long travelId,
                                                      @RequestBody TravelDateTitleUpdateRequestDto requestDto,
                                                      @LoginUser SessionUser sessionUser) {

        return ResponseEntity.ok(travelService.updateTravelDateTitle(sessionUser.getUserId(), travelId, requestDto));
    }

    // Schedules

    @GetMapping("/{travelId}/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> showSchedule(@PathVariable Long travelId,
                                                            @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @PostMapping("/{travelId}/schedules")
    public ResponseEntity<SimpleScheduleResponseDto> createSchedule(@PathVariable Long travelId,
                                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                    @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(travelId, date, scheduleCreateRequestDto));
    }

    @PutMapping("/{travelId}/schedules/{scheduleId}")
    public ResponseEntity<SimpleScheduleResponseDto> updateSchedule(@PathVariable Long travelId,
                                               @PathVariable Long scheduleId,
                                               @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, scheduleUpdateRequestDto));
    }

//    @GetMapping("/{travelId}/photos")
//    public ResponseEntity<Void> showTravelPhotos(@LoginUser SessionUser sessionUser,
//        @PathVariable Long travelId
//        ) {
//
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/{travelId}/schedules/{scheduleId}/photos")
    public ResponseEntity<List<SchedulePhotoResponseDto>> uploadSchedulePhotos(@LoginUser SessionUser sessionUser,
                                                     @PathVariable String travelId,
                                                     @PathVariable Long scheduleId,
                                                     @RequestPart List<MultipartFile> photos) {
        return ResponseEntity.ok(scheduleService.uploadSchedulePhotos(sessionUser.getUserId(), scheduleId, photos));
    }

    @PostMapping("/{travelId}/change")
    public ResponseEntity<List<Long>> changeScheduleOrder(@PathVariable Long travelId,
                                                    @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                    @RequestBody ScheduleOrderUpdateRequestDto requestDto) {
        return ResponseEntity.ok(travelService.changeScheduleOrder(travelId, date, requestDto));
    }

    // Costs

//    @GetMapping("/{travelId}/costs")
//    public ResponseEntity<List<CostResponseDto>> showCostsByTravel(@PathVariable Long travelId,
//                                                                         @LoginUser SessionUser sessionUser) {
//        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId, sessionUser.getUserId()));
//    }

    @GetMapping("/{travelId}/costs/{costId}")
    public CostResponseDto showCost(@PathVariable Long costId,
        @PathVariable Long travelId) {
        return this.costService.getCostById(costId);
    }

    @PostMapping("/{travelId}/costs")
    public CostCreateResponseDto createCost(
        @Valid @RequestBody CostCreateRequestDto costCreateRequestDto,
        @PathVariable Long travelId) {
        return costService.createCost(
            costCreateRequestDto, travelId
        );
    }

    @PutMapping("/{travelId}/costs/{costId}")
    public ResponseEntity<CostResponseDto> updateCost(@PathVariable Long travelId,
                                           @PathVariable Long costId,
                                           @RequestBody CostUpdateDto costUpdateDto) {
        return ResponseEntity.ok(this.costService.updateCostById(costId, costUpdateDto));
    }

    @PatchMapping("/{travelId}/costs/{costId}/userCosts/{userCostId}")
    public ResponseEntity<KakaoMessageResponse> calculateUserCost(@PathVariable Long travelId,
                                                                  @PathVariable Long costId,
                                                                  @PathVariable Long userCostId,
                                                                  @LoginUser SessionUser sessionUser,
                                                                  @RequestBody CostCalculateRequestDto requestDto) {
        if (Objects.nonNull(sessionUser.getAccessToken())) {
            throw new KakaoNotAuthenticationExcpetion("카카오 계정 인증이 필요합니다.", ErrorCode.KAKAO_NOT_AUTHENTICATION);
        }
        return ResponseEntity.ok(costService.calculateCost(userCostId, sessionUser, requestDto));
    }

    @DeleteMapping("/{travelId}/costs/{costId}")
    public ResponseEntity<Long> deleteCost(@PathVariable Long travelId,
        @PathVariable Long costId) {
        costService.deleteCostById(costId);
        return ResponseEntity.ok(costId);
    }

    // TODO 준비물: checked, title, 한 명 지정

    @GetMapping("/{travelId}/materials")
    public ResponseEntity<List<MaterialResponseDto>> getMaterials(@PathVariable Long travelId) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{travelId}/materials")
    public ResponseEntity<MaterialResponseDto> createMaterial(
        @PathVariable Long travelId,
        @RequestBody MaterialCreateRequestDto requestDto
    ) {
        MaterialResponseDto responseDto = materialService.createMaterial(travelId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{travelId}/materials/{materialId}")
    public ResponseEntity<MaterialResponseDto> updateMaterial(
        @PathVariable Long travelId,
        @PathVariable Long materialId,
        @RequestBody MaterialUpdateRequestDto requestDto
    ) {
        MaterialResponseDto responseDto = materialService.updateMaterial(materialId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{travelId}/materials/{materialId}")
    public ResponseEntity<Long> deleteMaterial(
        @PathVariable Long travelId,
        @PathVariable Long materialId
    ) {
        materialService.deleteMaterial(travelId, materialId);
        return ResponseEntity.ok(materialId);
    }

    // TravelTransaction

    @PostMapping("/{travelId}/transaction")
    public ResponseEntity<TravelTransactionCreateResponseDto> createTravelTransaction(@PathVariable Long travelId,
                                        @LoginUser SessionUser sessionUser,
                                        @Valid @RequestBody TravelTransactionCreateRequestDto travelTransactionCreateRequestDto) {
        return ResponseEntity.ok(this.travelService.createTravelTransaction(travelId, sessionUser.getUserId(), travelTransactionCreateRequestDto));
    }

    @GetMapping("/{travelId}/transaction")
    public ResponseEntity<TravelTransactionResponseDto> getAllTravelTransactionsByUserId(@PathVariable Long travelId,
                                              @LoginUser SessionUser sessionUser) {
        return ResponseEntity.ok(this.travelService.getAllTravelTransactionsByUserId(travelId, sessionUser.getUserId()));
    }

    @PutMapping("/{travelId}/transaction/{travelTransactionId}")
    public ResponseEntity<Void> updateTravelTransaction(@PathVariable Long travelId,
                                                        @PathVariable Long travelTransactionId,
                                                        @LoginUser SessionUser sessionUser,
                                                        @RequestBody TravelTransactionUpdateDto travelTransactionUpdateDto) {
        this.travelService.updateTravelTransaction(travelTransactionId, sessionUser.getUserId(), travelTransactionUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{travelId}/transaction/{travelTransactionId}")
    public ResponseEntity<Void> deleteTravelTransaction(@PathVariable Long travelId,
                                                        @PathVariable Long travelTransactionId) {
        this.travelService.deleteTravelTransaction(travelTransactionId);
        return ResponseEntity.ok().build();
    }
}
