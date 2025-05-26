package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.User;

import java.time.LocalDate;

public interface NutritionService {
    DailyNutrition calculateNutritionTargets(String userId);

    DailyNutrition getAverageNutrition(String userId, LocalDate start, LocalDate end);

    User getUserInfo(String userId);

}
