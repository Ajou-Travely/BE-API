package com.ajou.travely.controller.cost;

import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.CostUpdateDto;
import com.ajou.travely.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/v1/costs/{costId}")
    public CostResponseDto getCostById(@PathVariable Long costId) {
        return this.costService.getCostById(costId);
    }

    @PatchMapping("/api/v1/costs/{costId}")
    public ResponseEntity<Void> updateCostById(@PathVariable Long costId, CostUpdateDto costUpdateDto) {
        this.costService.updateCostById(costId, costUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/costs/{costId}")
    public ResponseEntity<Void> deleteCostById(@PathVariable Long costId) {
        this.costService.deleteCostById(costId);
        return ResponseEntity.ok().build();
    }
}
