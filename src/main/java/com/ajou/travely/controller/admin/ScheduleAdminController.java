package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/schedules")
@RestController
public class ScheduleAdminController {

    private final ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<Page<SimpleScheduleResponseDto>> showAllSchedules(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getAllSchedules(pageable));
    }
}
