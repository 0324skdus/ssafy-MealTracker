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
}
