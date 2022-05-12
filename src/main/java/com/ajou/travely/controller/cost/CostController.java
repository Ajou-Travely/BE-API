package com.ajou.travely.controller.cost;

import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/costs")
@RestController
@RequiredArgsConstructor
public class CostController {

    private final CostService costService;

    @PostMapping("")
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

    @GetMapping("/{costId}")
    public CostResponseDto getCostById(@PathVariable Long costId) {
        return this.costService.getCostById(costId);
    }
}
