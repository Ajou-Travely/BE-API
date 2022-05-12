package com.ajou.travely.controller.travel;

import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@RequestMapping("/api/v1/travel")
@RequiredArgsConstructor
@RestController
public class TravelController {
    private final TravelService travelService;

    @GetMapping("/api/v1/travels")
    public List<SimpleTravelResponseDto> getAllTravels() {
        return travelService.getAllTravels();
    }

    @PostMapping("/api/v1/travels")
    public ResponseEntity<Long> createTravel(Long userId,
                                          @Valid @RequestBody TravelCreateRequestDto travelCreateRequestDto) {
        Long travelId = travelService.createTravel(userId, travelCreateRequestDto);
        return ResponseEntity.ok(travelId);
    }

    @PostMapping("/api/v1/travels/{travelId}/users/{userId}")
    public ResponseEntity<Void> addUserToTravel(@PathVariable Long travelId, @PathVariable Long userId) {
        travelService.addUserToTravel(travelId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/travels/{travelId}/costs")
    public ResponseEntity<List<SimpleCostResponseDto>> getCostsByTravelId(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getCostsByTravelId(travelId));
    }
}
