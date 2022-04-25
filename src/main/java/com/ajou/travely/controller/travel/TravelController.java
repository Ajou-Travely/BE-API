package com.ajou.travely.controller.travel;

import com.ajou.travely.controller.travel.dto.travel.TravelResponseDto;
import com.ajou.travely.controller.travel.dto.travel.TravelCreateRequestDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RequestMapping("/api/v1/travel")
@RequiredArgsConstructor
@RestController
public class TravelController {
    private final TravelService travelService;

    @GetMapping("/api/v1/travels")
    public List<TravelResponseDto> getAllTravels() {
        return travelService.getAllTravels();
    }

    @PostMapping("/api/v1/travels")
    public TravelResponseDto createTravel(@RequestBody TravelCreateRequestDto travelCreateRequestDto) {
        return travelService.createTravel(travelCreateRequestDto);
    }

    @PostMapping("/api/v1/travels/{travelId}/add/{userId}")
    public TravelResponseDto addUserToTravel(@PathVariable Long travelId, @PathVariable Long userId) {
        travelService.addUserToTravel(travelId, userId);
        return new TravelResponseDto(travelService.getTravelById(travelId));
    }
}
