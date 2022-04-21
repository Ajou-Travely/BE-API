package com.ajou.travely.controller.travel;

import com.ajou.travely.controller.travel.dto.TravelSaveRequestDto;
import com.ajou.travely.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("/api/v1/travel")
@RequiredArgsConstructor
@RestController
public class TravelController {
    private final TravelService travelService;

    @PostMapping("/api/v1/travel")
    public Long save(@RequestBody TravelSaveRequestDto requestDto) {
        return travelService.save(requestDto);
    }
}
