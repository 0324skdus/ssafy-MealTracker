package com.ssafy.yamyam.dto;

public class DailyNutrition {
    private double kcal;
    private double carb;
    private double protein;
    private double fat;
    private double fiber;
    private double sugar;

    public DailyNutrition() {}

    public DailyNutrition(double kcal, double carb, double protein, double fat, double fiber, double sugar) {
        this.kcal = kcal;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.sugar = sugar;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getCarb() {
        return carb;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }
}
