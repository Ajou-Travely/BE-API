package com.ajou.travely.controller.place;

import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.service.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("api/v1/places")
    public List<PlaceResponseDto> getAllPlaces() {
        return placeService.getAllPlaces().stream().map(PlaceResponseDto::new).collect(Collectors.toList());
    }
}
