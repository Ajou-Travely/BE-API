package com.ajou.travely.controller.cost;

import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CostController {
    private final CostService costService;

    @PostMapping("/api/v1/costs")
    public CostCreateResponseDto createCost(@Valid @RequestBody CostCreateRequestDto costCreateRequestDto) {
        CostCreateResponseDto costCreateResponseDto = costService.createCost(
                costCreateRequestDto.getTotalAmount(),
                costCreateRequestDto.getTravelId(),
                costCreateRequestDto.getTitle(),
                costCreateRequestDto.getContent(),
                costCreateRequestDto.getIsEquallyDivided(),
                costCreateRequestDto.getAmountsPerUser(),
                costCreateRequestDto.getPayerId()
        );
        return costCreateResponseDto;
    }
}
