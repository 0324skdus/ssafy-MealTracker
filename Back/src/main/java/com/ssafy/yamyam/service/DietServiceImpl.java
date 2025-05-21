package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.Food;
import com.ssafy.yamyam.dto.MonthlyDiet;
import com.ssafy.yamyam.dto.User;

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

    @Autowired
    public DietServiceImpl(DietDao dietDao, UserService userService, FoodDao foodDao, NutritionService nutritionService) {
        this.dietDao = dietDao;
        this.userService = userService;
        this.foodDao = foodDao;
        this.nutritionService = nutritionService;
    }

    @Override
    public boolean register(Diet diet) {
        return dietDao.insert(diet) > 0;
    }

    @Override
    public boolean registerByFoodId(String userId, String mealType, int foodId, LocalDate date) {
        Food food = foodDao.selectById(foodId);
        if (food == null) return false;

        Diet diet = new Diet();
        diet.setUserId(userId);
        diet.setFoodId(foodId);
        diet.setMealType(mealType);
        diet.setFoodName(food.getFoodName());
        diet.setKcal(food.getKcal());
        diet.setCarbo(food.getCarbo());
        diet.setProtein(food.getProtein());
        diet.setFat(food.getFat());
        diet.setSugar(food.getSugar());
        diet.setFiber(food.getFiber());
        diet.setTransfat(food.getTransfat());
        diet.setSaturatedFat(food.getSaturatedFat());
        diet.setDate(date);

        return dietDao.insert(diet) > 0;
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


    //캘린더용 한달식단 조회
    @Override
    public List<MonthlyDiet> getMonthlySummary(String userId, LocalDate startDate, LocalDate endDate) {
        return dietDao.selectMonthlyDietSummary(userId, startDate, endDate);
    }


}
