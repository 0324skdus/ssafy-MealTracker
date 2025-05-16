package com.ssafy.yamyam.dto;

public class Food {
    private int foodId;
    private String foodName;
    private double kcal;
    private double carbo;
    private double protein;
    private double fat;
    private double sugar;
    private double fiber;
    private double transfat;
    private double saturatedFat;

    public Food() {
    }

    public Food(int foodId, String foodName, double kcal, double carbo, double protein, double fat,
                double sugar, double fiber, double transfat, double saturatedFat) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.kcal = kcal;
        this.carbo = carbo;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.fiber = fiber;
        this.transfat = transfat;
        this.saturatedFat = saturatedFat;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getCarbo() {
        return carbo;
    }

    public void setCarbo(double carbo) {
        this.carbo = carbo;
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

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getTransfat() {
        return transfat;
    }

    public void setTransfat(double transfat) {
        this.transfat = transfat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }
}
