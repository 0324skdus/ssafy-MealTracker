package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.DailyScore;
import com.ssafy.yamyam.service.DailyScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/score")
public class DailyScoreController {

    @Autowired
    private DailyScoreService dailyScoreService;



    @PostMapping("/calculate")
    public void calculateScore(@RequestParam("userId") String userId, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        dailyScoreService.calculateAndSaveDailyScore(userId, date);
    }

    @GetMapping("/{userId}/{date}")
    public DailyScore getScore(@PathVariable String userId, @PathVariable LocalDate date) {
        return dailyScoreService.getDailyScore(userId, date);
    }

    @GetMapping("/{userId}")
    public List<DailyScore> getAllScores(@PathVariable String userId) {
        return dailyScoreService.getAllScores(userId);
    }

    @GetMapping("/{userId}/average")
    public double getAverageScoreByPeriod(
            @PathVariable("userId") String userId,
            @RequestParam("period") String period
    ) {
        LocalDate end = LocalDate.now();
        LocalDate start = switch (period) {
            case "1W" -> end.minusWeeks(1);
            case "1M" -> end.minusMonths(1);
            case "3M" -> end.minusMonths(3);
            case "6M" -> end.minusMonths(6);
            case "1Y" -> end.minusYears(1);
            default -> throw new IllegalArgumentException("잘못된 기간");
        };
        return dailyScoreService.calculateAverageScore(userId, start, end);
    }
}
