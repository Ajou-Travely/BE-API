package com.ajou.travely.controller.place;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("api/v1/places")
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("")
    public List<PlaceResponseDto> getAllPlaces() {
        return placeService.getAllPlaces().stream().map(PlaceResponseDto::new).collect(Collectors.toList());
    }

    @PostMapping("")
    public PlaceResponseDto createPlace(@RequestBody @Valid PlaceCreateRequestDto placeCreateRequestDto) {
        return new PlaceResponseDto(placeService.insertPlace(placeCreateRequestDto.toEntity()));
    }
}
