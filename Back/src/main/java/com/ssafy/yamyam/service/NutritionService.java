package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.DailyNutrition;

public interface NutritionService {
    DailyNutrition calculateNutritionTargets(String userId);
}
