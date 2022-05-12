package com.ajou.travely.controller.travel;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1/travels")
@RequiredArgsConstructor
@RestController
public class TravelController {
    private final TravelService travelService;

    @GetMapping("")
    public List<SimpleTravelResponseDto> showAllTravels() {
        return travelService.getAllTravels();
    }

    @PostMapping("")
    public ResponseEntity<Long> createTravel(Long userId,
                                          @Valid @RequestBody TravelCreateRequestDto travelCreateRequestDto) {
        Long travelId = travelService.createTravel(userId, travelCreateRequestDto);
        return ResponseEntity.ok(travelId);
    }

    @PostMapping("/{travelId}/users/{userId}")
    public ResponseEntity<Void> addUserToTravel(@PathVariable Long travelId, @PathVariable Long userId) {
        travelService.addUserToTravel(travelId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> showCostsByTravelId(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId));
    }

    @GetMapping("/{travelId}/schedules")
    public ResponseEntity<List<SimpleScheduleResponseDto>> showSchedulesByTravelId(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getSchedulesByTravelId(travelId));
    }
}
