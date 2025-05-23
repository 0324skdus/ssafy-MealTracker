package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.DailyScore;

import java.time.LocalDate;
import java.util.List;

public interface DailyScoreService {
    void calculateAndSaveDailyScore(String userId, LocalDate date);
    DailyScore getDailyScore(String userId, LocalDate date);
    List<DailyScore> getAllScores(String userId);

    double calculateAverageScore(String userId, LocalDate start, LocalDate end);
}
