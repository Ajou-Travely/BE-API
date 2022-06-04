package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.admin.dto.AdminTravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/travels")
@RestController
public class TravelAdminController {

    private final TravelService travelService;

    @GetMapping()
    public ResponseEntity<Page<SimpleTravelResponseDto>> showAllTravels(@PageableDefault(
            sort = {"id"},
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return ResponseEntity.ok(travelService.getAllTravels(pageable));
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getTravelById(travelId, -1L));
    }

//    @PostMapping()
//    public ResponseEntity<Long> createTravel(@RequestBody AdminTravelCreateRequestDto requestDto) {
//        return ResponseEntity.ok(travelService.createTravel())
//    }
}
