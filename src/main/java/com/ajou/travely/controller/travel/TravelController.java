package com.ajou.travely.controller.travel;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public TravelResponseDto createTravel(@Valid @RequestBody TravelCreateRequestDto travelCreateRequestDto) {
        Travel travel = travelService.insertTravel(travelCreateRequestDto.toEntity());
        return new TravelResponseDto(travel);
    }

    @PostMapping("/api/v1/travels/{travelId}/users/{userId}")
    public TravelResponseDto addUserToTravel(@PathVariable Long travelId, @PathVariable Long userId) {
        travelService.addUserToTravel(travelId, userId);
        return new TravelResponseDto(travelService.getTravelById(travelId));
    }
}
