package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/average")
    public DailyNutrition getAverageNutrition(
            @RequestParam("userId") String userId,
            @RequestParam("period") String period
    ) {
        LocalDate end = LocalDate.now();
        LocalDate start = switch (period) {
            case "1W" -> end.minusWeeks(1);
            case "1M" -> end.minusMonths(1);
            case "3M" -> end.minusMonths(3);
            case "6M" -> end.minusMonths(6);
            case "1Y" -> end.minusYears(1);
            default -> throw new IllegalArgumentException("잘못된 기간: " + period);
        };
        return nutritionService.getAverageNutrition(userId, start, end);
    }
}
