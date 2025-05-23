package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dto.DailyScore;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyScoreServiceImpl implements DailyScoreService {

	private final DailyScoreDao dailyScoreDao;
    private final DietDao dietDao;
    private final UserService userService;
    private final NutritionService nutritionService;

    @Autowired
    public DailyScoreServiceImpl(
        DailyScoreDao dailyScoreDao,
        DietDao dietDao,
        UserService userService,
        NutritionService nutritionService
    ) {
        this.dailyScoreDao = dailyScoreDao;
        this.dietDao = dietDao;
        this.userService = userService;
        this.nutritionService = nutritionService;
    }

    @Override
    public void calculateAndSaveDailyScore(String userId, LocalDate date) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("date", date);
        List<Diet> diets = dietDao.selectByUserIdAndDate(userId, date);

        double totalKcal = 0, totalCarbo = 0, totalProtein = 0, totalFat = 0;
        double totalSaturatedfat = 0, totalSugar = 0, totalFiber = 0, totalTransfat = 0;

        for (Diet d : diets) {
            totalKcal += d.getKcal();
            totalCarbo += d.getCarbo();
            totalProtein += d.getProtein();
            totalFat += d.getFat();
            totalSaturatedfat += d.getSaturatedFat();
            totalSugar += d.getSugar();
            totalFiber += d.getFiber();
            totalTransfat += d.getTransfat();
        }

        double score = calculateScore(userId, date);

        DailyScore dailyScore = new DailyScore();
        dailyScore.setUserId(userId);
        dailyScore.setDate(date);
        dailyScore.setTotalKcal(totalKcal);
        dailyScore.setTotalCarbo(totalCarbo);
        dailyScore.setTotalProtein(totalProtein);
        dailyScore.setTotalFat(totalFat);
        dailyScore.setTotalSaturatedfat(totalSaturatedfat);
        dailyScore.setTotalSugar(totalSugar);
        dailyScore.setTotalFiber(totalFiber);
        dailyScore.setTotalTransfat(totalTransfat);
        dailyScore.setScore(score);

        DailyScore existing = dailyScoreDao.selectByUserIdAndDate(param);
        if (existing == null) {
            dailyScoreDao.insert(dailyScore);
        } else {
            dailyScoreDao.update(dailyScore);
        }System.out.println("[점수 계산 호출] userId=" + userId + ", date=" + date);
    }

    private double calculateScore(String userId, LocalDate date) {
    	User user = userService.getUserById(userId);
        if (user == null) return -1;

        List<Diet> meals = dietDao.selectByUserIdAndDate(userId, date);
        if (meals == null || meals.isEmpty()) return 0;

        double recommendedCalories = nutritionService.calculateNutritionTargets(user.getUserId()).getKcal();

        String goal = user.getSpecialNote(); // "체중감량", "체중유지", "근육량증가"

        double totalCal = 0, totalCarb = 0, totalProtein = 0, totalFat = 0;
        double totalSugar = 0, totalFiber = 0, totalTransFat = 0, totalSaturatedFat = 0;

        for (Diet meal : meals) {
            totalCal += meal.getKcal();
            totalCarb += meal.getCarbo();
            totalProtein += meal.getProtein();
            totalFat += meal.getFat();
            totalSugar += meal.getSugar();
            totalFiber += meal.getFiber();
            totalTransFat += meal.getTransfat();
            totalSaturatedFat += meal.getSaturatedFat();
        }

        int score = 0;
        double totalMacro = totalCarb + totalProtein + totalFat;
        double carbRatio = totalMacro > 0 ? (totalCarb / totalMacro) * 100 : 0;
        double proteinRatio = totalMacro > 0 ? (totalProtein / totalMacro) * 100 : 0;
        double fatRatio = totalMacro > 0 ? (totalFat / totalMacro) * 100 : 0;

        switch (goal) {
            case "체중감량" -> {
                if (totalCal >= recommendedCalories * 0.8 && totalCal <= recommendedCalories * 0.9)
                    score += 30;
                if (proteinRatio >= 25) score += 10;
                if (totalSugar <= 25) score += 10;
                if (fatRatio <= 25) score += 10;
                if (totalSaturatedFat <= 10) score += 10;
                if (totalFiber >= 20) score += 10;
                break;
            }
            case "체중유지" -> {
                if (totalCal >= recommendedCalories * 0.9 && totalCal <= recommendedCalories * 1.1)
                    score += 30;

                int balanceCount = 0;
                if (carbRatio >= 50 && carbRatio <= 60) balanceCount++;
                if (proteinRatio >= 20 && proteinRatio <= 25) balanceCount++;
                if (fatRatio >= 15 && fatRatio <= 25) balanceCount++;
                if (balanceCount == 3) score += 30;
                else if (balanceCount == 2) score += 20;
                else if (balanceCount == 1) score += 10;

                if (totalTransFat == 0) score += 20;
                else if (totalTransFat <= 0.5) score += 10;

                if (totalSaturatedFat <= 15) score += 10;
                if (totalSugar <= 50) score += 10;
                if (totalFiber >= 20) score += 10;
                else if (totalFiber >= 10) score += 5;
                break;
            }
            case "근육량증가" -> {
                if (totalCal >= recommendedCalories * 1.1 && totalCal <= recommendedCalories * 1.2)
                    score += 30;
                if (proteinRatio >= 30) score += 20;
                if (carbRatio >= 40) score += 10;
                if (fatRatio <= 25) score += 10;
                if (totalSaturatedFat <= 20) score += 5;
                if (totalFiber >= 20) score += 10;
                break;
            }
            default -> {
                if (totalCal >= recommendedCalories * 0.9 && totalCal <= recommendedCalories * 1.1)
                    score += 30;

                int balanceCount = 0;
                if (carbRatio >= 50 && carbRatio <= 60) balanceCount++;
                if (proteinRatio >= 20 && proteinRatio <= 25) balanceCount++;
                if (fatRatio >= 15 && fatRatio <= 25) balanceCount++;
                if (balanceCount == 3) score += 30;
                else if (balanceCount == 2) score += 20;
                else if (balanceCount == 1) score += 10;

                if (totalTransFat == 0) score += 20;
                else if (totalTransFat <= 0.5) score += 10;

                if (totalSaturatedFat <= 15) score += 10;
                if (totalSugar <= 50) score += 10;
                if (totalFiber >= 20) score += 10;
                else if (totalFiber >= 10) score += 5;
                break;
            }
        }

        return score;
    }

    @Override
    public DailyScore getDailyScore(String userId, LocalDate date) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("date", date);
        return dailyScoreDao.selectByUserIdAndDate(param);
    }

    @Override
    public List<DailyScore> getAllScores(String userId) {
        return dailyScoreDao.selectByUserId(userId);
    }
}
