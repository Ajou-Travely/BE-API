package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/travels")
@RestController
public class TravelAdminController {

    private final TravelService travelService;

    @GetMapping()
    public ResponseEntity<Page<SimpleTravelResponseDto>> showAllTravels(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(travelService.getAllTravels(pageable));
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelResponseDto> showTravel(@PathVariable Long travelId) {
        return ResponseEntity.ok(travelService.getTravelById(travelId, -1L));
    }
}
