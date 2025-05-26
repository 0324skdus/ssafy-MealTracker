package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DietServiceImpl implements DietService {

    private final DietDao dietDao;
    private final UserService userService;
    private final FoodDao foodDao;
    private final NutritionService nutritionService;
    private final DailyScoreDao dailyScoreDao;
    private final DailyScoreService dailyScoreService;

    @Autowired
    public DietServiceImpl(DietDao dietDao,
                           UserService userService,
                           FoodDao foodDao,
                           NutritionService nutritionService,
                           DailyScoreDao dailyScoreDao,
                           DailyScoreService dailyScoreService) {
        this.dietDao = dietDao;
        this.userService = userService;
        this.foodDao = foodDao;
        this.nutritionService = nutritionService;
        this.dailyScoreDao = dailyScoreDao;
        this.dailyScoreService = dailyScoreService;
    }

    @Override
    public boolean register(Diet diet) {
        boolean result = dietDao.insert(diet) > 0;
        if (result) {
            dailyScoreService.calculateAndSaveDailyScore(diet.getUserId(), diet.getDate());
        }
        return result;
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
        diet.setWeight(weight); // 계산용 무게

        boolean inserted = dietDao.insert(diet) > 0;
        if (inserted) {
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
        Diet target = dietDao.selectById(id);
        if (target == null) return false;

        String userId = target.getUserId();
        LocalDate date = target.getDate();

        boolean deleted = dietDao.delete(id) > 0;
        if (!deleted) return false;

        List<Diet> remaining = dietDao.selectByUserIdAndDate(userId, date);

        if (remaining.isEmpty()) {
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("date", date);
            dailyScoreDao.deleteByUserAndDate(userId, date);
        } else {
            dailyScoreService.calculateAndSaveDailyScore(userId, date);
        }

        return true;
    }

    @Override
    public List<Diet> getByUserIdAndDate(String userId, LocalDate date) {
        return dietDao.selectByUserIdAndDate(userId, date);
    }

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
                transfat / count,
                0  // bmr은 평균에 포함되지 않으므로 기본값
        );
    }

    public double calculateAverageScore(String userId, LocalDate start, LocalDate end) {
        List<DailyScore> scores = dailyScoreDao.selectScoresByUserAndPeriod(userId, start, end);
        if (scores.isEmpty()) return 0;
        double total = scores.stream().mapToDouble(DailyScore::getScore).sum();
        return total / scores.size();
    }

    public Diet registerAndReturn(Diet diet) {
        dietDao.insert(diet);
        return diet;
    }
}
