package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dto.DailyScore;
import com.ssafy.yamyam.dto.Diet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyScoreServiceImpl implements DailyScoreService {

    @Autowired
    private DailyScoreDao dailyScoreDao;

    @Autowired
    private DietDao dietDao;

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

        double score = calculateScore(totalKcal, totalCarbo, totalProtein, totalFat);

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
    }

    private double calculateScore(double kcal, double carbo, double protein, double fat) {
        if (kcal <= 1800) return 100;
        else if (kcal <= 2200) return 80;
        else return 60;
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
