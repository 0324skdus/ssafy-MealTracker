package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

        // 1. BMR 계산 (Harris-Benedict)
        double bmr;
        if (user.getCustomBmr() != null) {
            bmr = user.getCustomBmr();  // 사용자가 직접 입력한 BMR
        } else if ("M".equals(user.getGender())) {
            bmr = 66.47 + (13.75 * user.getWeight()) + (5.003 * user.getHeight()) - (6.755 * user.getAge());
        } else {
            bmr = 655.1 + (9.563 * user.getWeight()) + (1.850 * user.getHeight()) - (4.676 * user.getAge());
        }

        // 2. 활동지수 적용
        double activityFactor = switch (user.getActivityLevel()) {
            case "L" -> 1.2;
            case "M" -> 1.55;
            case "H" -> 1.725;
            default -> 1.2;
        };

        double tdee = bmr * activityFactor;

        // 3. 목표에 따른 보정된 총 섭취 kcal
        double totalKcal = tdee;
        if ("근육량 증가".equals(user.getSpecialNote())) {
            totalKcal += 400; // +350~470kcal 범위 중간값
        }

        // 4. 단백질 g/kg 기준 계산
        double proteinPerKg;
        if ("체중감량".equals(user.getSpecialNote())) {
            proteinPerKg = switch (user.getActivityLevel()) {
                case "L" -> 1.6;
                case "M" -> 1.8;
                case "H" -> 2.2;
                default -> 1.6;
            };
        } else if ("근육량 증가".equals(user.getSpecialNote())) {
            proteinPerKg = switch (user.getActivityLevel()) {
                case "L" -> 1.6;
                case "M" -> 1.65;
                case "H" -> 1.7;
                default -> 1.6;
            };
        } else { // 체중유지
            proteinPerKg = switch (user.getActivityLevel()) {
                case "L" -> 1.4;
                case "M" -> 1.5;
                case "H" -> 1.6;
                default -> 1.4;
            };
        }
        double protein = proteinPerKg * user.getWeight();
        double proteinKcal = protein * 4;

        // 5. 지방 % 기준 계산
        double fatRatio;
        if ("체중감량".equals(user.getSpecialNote())) {
            fatRatio = 0.25;
        } else if ("근육량 증가".equals(user.getSpecialNote())) {
            fatRatio = 0.20;
        } else {
            fatRatio = 0.27; // 유지: 중간값
        }
        double fatKcal = totalKcal * fatRatio;
        double fat = fatKcal / 9;

        // 6. 탄수화물: 나머지 kcal 사용, 최소 보장
        double remainingKcal = totalKcal - (proteinKcal + fatKcal);
        double carb = remainingKcal / 4;

        // 최소 탄수화물 g/kg 보장
        double minCarbPerKg = switch (user.getSpecialNote()) {
            case "체중감량" -> 3.0;
            case "근육량 증가" -> switch (user.getActivityLevel()) {
                case "L" -> 5.0;
                case "M" -> 6.0;
                case "H" -> 7.0;
                default -> 5.0;
            };
            default -> 4.5; // 유지
        };
        double minCarb = user.getWeight() * minCarbPerKg;
        if (carb < minCarb) carb = minCarb;

        // 7. 기타
        double fiber = "M".equals(user.getGender()) ? 25 : 20;
        double sugar = Math.min(totalKcal * 0.15 / 4, 50);
        double transfat = totalKcal * 0.01 / 9;

        return new DailyNutrition(
                (double) Math.round(totalKcal),
                (double) Math.round(carb),
                (double) Math.round(protein),
                (double) Math.round(fat),
                fiber,
                sugar,
                (double) Math.round(transfat),
                (double) Math.round(bmr)
        );
    }

    @Override
    public DailyNutrition getAverageNutrition(String userId, LocalDate start, LocalDate end) {
        List<Diet> diets = dietDao.selectDietByUserAndPeriod(userId, start, end);
        if (diets.isEmpty()) {
            return new DailyNutrition(0, 0, 0, 0, 0, 0, 0, 0);
        }

        Map<LocalDate, List<Diet>> groupedByDate = new HashMap<>();
        for (Diet diet : diets) {
            groupedByDate
                    .computeIfAbsent(diet.getDate(), k -> new ArrayList<>())
                    .add(diet);
        }

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

        return new DailyNutrition(
                (double) Math.round(totalKcal / dayCount),
                (double) Math.round(totalCarb / dayCount),
                (double) Math.round(totalProtein / dayCount),
                (double) Math.round(totalFat / dayCount),
                (double) Math.round(totalFiber / dayCount),
                (double) Math.round(totalSugar / dayCount),
                (double) Math.round(totalTransfat / dayCount),
                0 // 평균 BMR은 없음
        );
    }

    @Override
    public User getUserInfo(String userId) {
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저 정보를 찾을 수 없습니다: " + userId);
        }
        return user;
    }
}
