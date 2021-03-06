package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/costs")
@RestController
public class CostAdminController {

    private final CostService costService;

    @GetMapping()
    public ResponseEntity<Page<SimpleCostResponseDto>> showAllCosts(@PageableDefault(
            sort = {"id"},
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return ResponseEntity.ok(costService.getAllCosts(pageable));
    }

    @GetMapping("/{costId}")
    public ResponseEntity<CostResponseDto> showCost(@PathVariable Long costId) {
        return ResponseEntity.ok(costService.getCostById(costId));
    }
}
