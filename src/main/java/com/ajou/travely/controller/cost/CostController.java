package com.ajou.travely.controller.cost;

import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.domain.Cost;
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

    @PostMapping("/api/v1/cost")
    public CostResponseDto createCost(@Valid @RequestBody CostCreateRequestDto costCreateRequestDto) {
        Cost cost = costService.insertCost(
                costCreateRequestDto.getTotalAmount(),
                costCreateRequestDto.getTravelId(),
                costCreateRequestDto.getTitle(),
                costCreateRequestDto.getContent(),
                costCreateRequestDto.getIsEquallyDivided(),
                costCreateRequestDto.getAmountsPerUser(),
                costCreateRequestDto.getPayerId()
        );
        return new CostResponseDto(cost);
    }
}
