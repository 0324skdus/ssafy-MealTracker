package com.ssafy.yamyam.dto;

import java.time.LocalDate;

public class DailyScore {
    private int id;
    private String userId;
    private LocalDate date;

    private double totalKcal;
    private double totalCarbo;
    private double totalProtein;
    private double totalFat;
    private double totalSaturatedfat;
    private double totalSugar;
    private double totalFiber;
    private double totalTransfat;
    private double score;

    public DailyScore() {}

    public DailyScore(int id, String userId, LocalDate date, double totalKcal, double totalCarbo, double totalProtein, double totalFat, double totalSaturatedfat, double totalSugar, double totalFiber, double totalTransfat, double score) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.totalKcal = totalKcal;
        this.totalCarbo = totalCarbo;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalSaturatedfat = totalSaturatedfat;
        this.totalSugar = totalSugar;
        this.totalFiber = totalFiber;
        this.totalTransfat = totalTransfat;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(double totalKcal) {
        this.totalKcal = totalKcal;
    }

    public double getTotalCarbo() {
        return totalCarbo;
    }

    public void setTotalCarbo(double totalCarbo) {
        this.totalCarbo = totalCarbo;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(double totalProtein) {
        this.totalProtein = totalProtein;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public double getTotalSaturatedfat() {
        return totalSaturatedfat;
    }

    public void setTotalSaturatedfat(double totalSaturatedfat) {
        this.totalSaturatedfat = totalSaturatedfat;
    }

    public double getTotalSugar() {
        return totalSugar;
    }

    public void setTotalSugar(double totalSugar) {
        this.totalSugar = totalSugar;
    }

    public double getTotalFiber() {
        return totalFiber;
    }

    public void setTotalFiber(double totalFiber) {
        this.totalFiber = totalFiber;
    }

    public double getTotalTransfat() {
        return totalTransfat;
    }

    public void setTotalTransfat(double totalTransfat) {
        this.totalTransfat = totalTransfat;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
