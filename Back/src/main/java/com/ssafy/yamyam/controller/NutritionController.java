package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    @Autowired
    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/target")
    public DailyNutrition calculateTarget(@RequestParam("userId") String userId) {
        return nutritionService.calculateNutritionTargets(userId);
    }
}
