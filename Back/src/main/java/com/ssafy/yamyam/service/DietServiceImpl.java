package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DietServiceImpl implements DietService {

    private final DietDao dietDao;
    private final UserService userService;
    private final FoodDao foodDao;
    private final NutritionService nutritionService;
    private final DailyScoreService dailyScoreService;

    @Autowired
    public DietServiceImpl(DietDao dietDao, UserService userService, FoodDao foodDao, NutritionService nutritionService, DailyScoreService dailyScoreService) {
    private final DailyScoreDao dailyScoreDao;


    @Autowired
    public DietServiceImpl(DietDao dietDao,
                           UserService userService,
                           FoodDao foodDao,
                           NutritionService nutritionService,
                           DailyScoreDao dailyScoreDao) {
        this.dietDao = dietDao;
        this.userService = userService;
        this.foodDao = foodDao;
        this.nutritionService = nutritionService;
        this.dailyScoreService = dailyScoreService;
        this.dailyScoreDao = dailyScoreDao;
    }

    @Override
    public boolean register(Diet diet) {
        return dietDao.insert(diet) > 0;
    }

    @Override
    public boolean registerByFoodId(String userId, String mealType, int foodId, LocalDate date, double weight) {
        Food food = foodDao.selectById(foodId);
        if (food == null) return false;
        
        double factor = (weight > 0) ? weight / 100.0 : 1.0;

        Diet diet = new Diet();
        diet.setUserId(userId);
        diet.setFoodId(foodId);
        diet.setMealType(mealType);
        diet.setFoodName(food.getFoodName());
        diet.setKcal(food.getKcal() * factor);
        diet.setCarbo(food.getCarbo() * factor);
        diet.setProtein(food.getProtein() * factor);
        diet.setFat(food.getFat() * factor);
        diet.setSugar(food.getSugar() * factor);
        diet.setFiber(food.getFiber() * factor);
        diet.setTransfat(food.getTransfat() * factor);
        diet.setSaturatedFat(food.getSaturatedFat() * factor);
        diet.setDate(date);
        diet.setWeight(weight); // 저장은 안하는 계산용 무게

        boolean inserted = dietDao.insert(diet) > 0;
        System.out.println("[DEBUG] inserted = " + inserted);
        if (inserted) {
            // 식단 등록 성공한 경우 점수 자동 계산
        	System.out.println("[DEBUG] diet 등록 성공, 점수 계산 시작: " + userId + ", " + date);
        	dailyScoreService.calculateAndSaveDailyScore(userId, date);
        }

        return inserted;
    }

    @Override
    public Diet getById(int dietId) {
        return dietDao.selectById(dietId);
    }

    @Override
    public List<Diet> getByUserId(String userId) {
        return dietDao.selectByUserId(userId);
    }

    @Override
    public boolean modify(Diet diet) {
        return dietDao.update(diet) > 0;
    }

    @Override
    public boolean remove(int id) {
        return dietDao.delete(id) > 0;
    }

    @Override
    public int scoring(String userId, LocalDate date) {
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
            }
            case "근육량증가" -> {
                if (totalCal >= recommendedCalories * 1.1 && totalCal <= recommendedCalories * 1.2)
                    score += 30;
                if (proteinRatio >= 30) score += 20;
                if (carbRatio >= 40) score += 10;
                if (fatRatio <= 25) score += 10;
                if (totalSaturatedFat <= 20) score += 5;
                if (totalFiber >= 20) score += 10;
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
            }
        }

        return score;
    }

    @Override
    public List<Diet> getByUserIdAndDate(String userId, LocalDate date) {
        return dietDao.selectByUserIdAndDate(userId, date);
    }

    //캘린더용 한달식단 조회
    @Override
    public List<MonthlyDiet> getMonthlySummary(String userId, LocalDate startDate, LocalDate endDate) {
        return dietDao.selectMonthlyDietSummary(userId, startDate, endDate);
    }

    public DailyNutrition calculateAverageNutrition(String userId, LocalDate start, LocalDate end) {
        List<Diet> diets = dietDao.selectDietByUserAndPeriod(userId, start, end);
        double kcal = 0, carbo = 0, protein = 0, fat = 0, fiber = 0, sugar = 0, transfat = 0;

        for (Diet d : diets) {
            kcal += d.getKcal();
            carbo += d.getCarbo();
            protein += d.getProtein();
            fat += d.getFat();
            fiber += d.getFiber();
            sugar += d.getSugar();
            transfat += d.getTransfat();
        }

        int count = diets.size() == 0 ? 1 : diets.size();
        return new DailyNutrition(
                kcal / count,
                carbo / count,
                protein / count,
                fat / count,
                fiber / count,
                sugar / count,
                transfat/ count

        );
    }

    public double calculateAverageScore(String userId, LocalDate start, LocalDate end) {
        List<DailyScore> scores = dailyScoreDao.selectScoresByUserAndPeriod(userId, start, end);
        if (scores.isEmpty()) return 0;
        double total = scores.stream().mapToDouble(DailyScore::getScore).sum();
        return total / scores.size();
    }

}
