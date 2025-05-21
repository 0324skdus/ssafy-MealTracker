package com.ssafy.yamyam.dto;

import java.time.LocalDate;

public class MonthlyDiet {
    private LocalDate date;      // 날짜 (yyyy-MM-dd)
    private int score;           // 하루 식단 점수
    private int totalKcal;       // 하루 총 칼로리

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(int totalKcal) {
        this.totalKcal = totalKcal;
    }
}
