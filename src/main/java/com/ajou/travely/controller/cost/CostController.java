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

@RequestMapping("/v1/costs")
@RestController
@RequiredArgsConstructor
public class CostController {

    private final CostService costService;

    @PostMapping("")
    public CostCreateResponseDto createCost(@Valid @RequestBody CostCreateRequestDto costCreateRequestDto) {
        return costService.createCost(
                costCreateRequestDto.getTotalAmount(),
                costCreateRequestDto.getTravelId(),
                costCreateRequestDto.getTitle(),
                costCreateRequestDto.getContent(),
                costCreateRequestDto.getIsEquallyDivided(),
                costCreateRequestDto.getAmountsPerUser(),
                costCreateRequestDto.getPayerId()
        );
    }

    @GetMapping("/{costId}")
    public CostResponseDto getCostById(@PathVariable Long costId) {
        return this.costService.getCostById(costId);
    }

    @PatchMapping("/v1/costs/{costId}")
    public ResponseEntity<Void> updateCostById(@PathVariable Long costId, CostUpdateDto costUpdateDto) {
        this.costService.updateCostById(costId, costUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/costs/{costId}")
    public ResponseEntity<Void> deleteCostById(@PathVariable Long costId) {
        this.costService.deleteCostById(costId);
        return ResponseEntity.ok().build();
    }
}
