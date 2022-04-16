package com.ajou.travely.controller;

import com.ajou.travely.service.TravelService;
import org.springframework.stereotype.Controller;

@Controller
public class TravelController {
    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }
}
