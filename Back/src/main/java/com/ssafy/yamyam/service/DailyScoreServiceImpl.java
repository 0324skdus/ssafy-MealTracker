package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dto.DailyScore;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.User;
import com.ssafy.yamyam.dto.DailyNutrition;
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
        }
        System.out.println("[점수 계산 호출] userId=" + userId + ", date=" + date);
    }

    private double calculateScore(String userId, LocalDate date) {
        User user = userService.getUserById(userId);
        if (user == null) return -1;

        List<Diet> meals = dietDao.selectByUserIdAndDate(userId, date);
        if (meals == null || meals.isEmpty()) return 0;

        DailyNutrition target = nutritionService.calculateNutritionTargets(userId);

        double actualKcal = 0, actualCarb = 0, actualProtein = 0, actualFat = 0;
        double actualFiber = 0, actualSugar = 0, actualTransFat = 0, actualSaturatedFat = 0;

        for (Diet meal : meals) {
            actualKcal += meal.getKcal();
            actualCarb += meal.getCarbo();
            actualProtein += meal.getProtein();
            actualFat += meal.getFat();
            actualSugar += meal.getSugar();
            actualFiber += meal.getFiber();
            actualTransFat += meal.getTransfat();
            actualSaturatedFat += meal.getSaturatedFat();
        }

        double targetKcal = target.getKcal();
        double targetCarb = target.getCarb();
        double targetProtein = target.getProtein();
        double targetFat = target.getFat();
        double targetFiber = target.getFiber();
        double targetTransFat = target.getTransfat();
        double targetSaturatedFat = targetKcal * 0.10 / 9;

        int kcalScore = scoreByRatio(actualKcal, targetKcal);
        int carbScore = scoreByRatio(actualCarb, targetCarb);
        int proteinScore = scoreByRatio(actualProtein, targetProtein);
        int fatScore = scoreByRatio(actualFat, targetFat);
        int fiberScore = scoreByRatio(actualFiber, targetFiber);
        int saturatedFatScore = reverseScoreByRatio(actualSaturatedFat, targetSaturatedFat);
        int transFatScore = reverseScoreByRatio(actualTransFat, targetKcal * 0.01 / 9);

        double finalScore =
                kcalScore * 0.15 +
                        carbScore * 0.20 +
                        proteinScore * 0.25 +
                        fatScore * 0.15 +
                        fiberScore * 0.10 +
                        saturatedFatScore * 0.10 +
                        transFatScore * 0.05;

        return Math.round(finalScore);
    }

    private int scoreByRatio(double actual, double target) {
        if (target == 0) return 100;
        double ratio = actual / target;

        if (ratio >= 0.9 && ratio <= 1.1) return 100;
        if ((ratio >= 0.8 && ratio < 0.9) || (ratio > 1.1 && ratio <= 1.2)) return 80;
        if ((ratio >= 0.6 && ratio < 0.8) || (ratio > 1.2 && ratio <= 1.4)) return 60;
        if ((ratio >= 0.4 && ratio < 0.6) || (ratio > 1.4 && ratio <= 1.7)) return 40;
        if ((ratio >= 0.2 && ratio < 0.4) || (ratio > 1.7 && ratio <= 2.0)) return 20;
        return 0;
    }

    private int reverseScoreByRatio(double actual, double maxAllowed) {
        if (actual == 0) return 100;
        if (actual <= maxAllowed * 0.8) return 100;
        if (actual <= maxAllowed) return 80;
        if (actual <= maxAllowed * 1.2) return 60;
        if (actual <= maxAllowed * 1.5) return 40;
        return 0;
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

    @Override
    public double calculateAverageScore(String userId, LocalDate start, LocalDate end) {
        List<DailyScore> scores = dailyScoreDao.selectScoresByUserAndPeriod(userId, start, end);
        if (scores.isEmpty()) return 0;

        double total = 0;
        int count = 0;

        for (DailyScore score : scores) {
            if (score.getScore() > 0) {
                total += score.getScore();
                count++;
            }
        }

        return count == 0 ? 0 : total / count;
    }
}
