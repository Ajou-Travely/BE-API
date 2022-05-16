package com.ajou.travely.controller.place;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("v1/places")
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("")
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PostMapping("")
    public ResponseEntity<PlaceResponseDto> createPlace(@RequestBody @Valid PlaceCreateRequestDto placeCreateRequestDto) {
        return ResponseEntity.ok(placeService.createPlace(placeCreateRequestDto));
    }
}
