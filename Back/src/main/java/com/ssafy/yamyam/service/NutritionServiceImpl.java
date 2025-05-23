package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NutritionServiceImpl implements NutritionService {

    private final UserDao userDao;
    private final DietDao dietDao;
    @Autowired
    public NutritionServiceImpl(UserDao userDao, DietDao dietDao) {
        this.userDao = userDao;
        this.dietDao = dietDao;
    }

    @Override
    public DailyNutrition calculateNutritionTargets(String userId) {
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        // BMR 계산 (Harris-Benedict)
        double bmr;
        if ("M".equals(user.getGender())) {
            bmr = 66.47 + (13.75 * user.getWeight()) + (5.003 * user.getHeight()) - (6.755 * user.getAge());
        } else {
            bmr = 655.1 + (9.563 * user.getWeight()) + (1.850 * user.getHeight()) - (4.676 * user.getAge());
        }

        // 활동지수 적용
        double activityFactor = switch (user.getActivityLevel()) {
            case "L" -> 1.2;
            case "M" -> 1.55;
            case "H" -> 1.725;
            default -> 1.2;
        };

        double tdee = bmr * activityFactor;

        // 목표별 탄단지 비율 설정
        double carbRatio, proteinRatio, fatRatio;
        switch (user.getSpecialNote()) {
            case "체중감량" -> {
                carbRatio = 0.4;
                proteinRatio = 0.3;
                fatRatio = 0.3;
            }
            case "근육량 증가" -> {
                carbRatio = 0.4;
                proteinRatio = 0.35;
                fatRatio = 0.25;
            }
            default -> { // 유지 or 기타
                carbRatio = 0.5;
                proteinRatio = 0.2;
                fatRatio = 0.3;
            }
        }

        // 영양소(g) 계산
        double kcal = tdee;
        double carb = kcal * carbRatio / 4;
        double protein = kcal * proteinRatio / 4;
        double fat = kcal * fatRatio / 9;

        // 식이섬유 (남: 25g, 여: 20g)
        double fiber = "M".equals(user.getGender()) ? 25 : 20;

        // 당류: 10~20% 제한 → 평균 15% 기준, clamp 50g
        double sugar = Math.min(kcal * 0.15 / 4, 50);

        double transfat = kcal * 0.01 / 9;

        return new DailyNutrition(
                Math.round(kcal),
                Math.round(carb),
                Math.round(protein),
                Math.round(fat),
                fiber,
                sugar,
                Math.round(transfat)
        );
    }

    @Override
    public DailyNutrition getAverageNutrition(String userId, LocalDate start, LocalDate end) {
        List<Diet> diets = dietDao.selectDietByUserAndPeriod(userId, start, end);
        if (diets.isEmpty()) {
            return new DailyNutrition(0, 0, 0, 0, 0, 0, 0);
        }

        // 날짜별로 그룹핑
        Map<LocalDate, List<Diet>> groupedByDate = new HashMap<>();
        for (Diet diet : diets) {
            groupedByDate
                    .computeIfAbsent(diet.getDate(), k -> new ArrayList<>())
                    .add(diet);
        }

        // 하루 단위 총합 누적
        double totalKcal = 0, totalCarb = 0, totalProtein = 0, totalFat = 0;
        double totalFiber = 0, totalSugar = 0, totalTransfat = 0;
        int dayCount = groupedByDate.size();

        for (List<Diet> oneDayDiets : groupedByDate.values()) {
            double dayKcal = 0, dayCarb = 0, dayProtein = 0, dayFat = 0;
            double dayFiber = 0, daySugar = 0, dayTransfat = 0;

            for (Diet d : oneDayDiets) {
                dayKcal += d.getKcal();
                dayCarb += d.getCarbo();
                dayProtein += d.getProtein();
                dayFat += d.getFat();
                dayFiber += d.getFiber();
                daySugar += d.getSugar();
                dayTransfat += d.getTransfat();
            }

            totalKcal += dayKcal;
            totalCarb += dayCarb;
            totalProtein += dayProtein;
            totalFat += dayFat;
            totalFiber += dayFiber;
            totalSugar += daySugar;
            totalTransfat += dayTransfat;
        }

        // 날짜 수로 나눈 하루 평균
        return new DailyNutrition(
                Math.round(totalKcal / dayCount),
                Math.round(totalCarb / dayCount),
                Math.round(totalProtein / dayCount),
                Math.round(totalFat / dayCount),
                Math.round(totalFiber / dayCount),
                Math.round(totalSugar / dayCount),
                Math.round(totalTransfat / dayCount)
        );
    }

}
