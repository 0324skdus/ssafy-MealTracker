package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutritionServiceImpl implements NutritionService {

    private final UserDao userDao;

    @Autowired
    public NutritionServiceImpl(UserDao userDao) {
        this.userDao = userDao;
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

        return new DailyNutrition(
                Math.round(kcal),
                Math.round(carb),
                Math.round(protein),
                Math.round(fat),
                fiber,
                sugar
        );
    }
}
