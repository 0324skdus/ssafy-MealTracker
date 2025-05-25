package com.ssafy.yamyam.dto;

import java.time.LocalDate;

public class Diet {
    private int dietId;
    private String userId;
    private String mealType; // 아침/점심/저녁
    private String foodName;
    private Integer foodId;
    private int score;
    private transient double weight; //MyBatis insert 대상에서 제외, 매핑오류방지


    private double kcal;
    private double carbo;
    private double protein;
    private double fat;
    private double sugar;
    private double fiber;
    private double transfat;
    private double saturatedFat;
    private LocalDate date;

    public Diet() {
    }

    public Diet(int dietId, String userId, String mealType, String foodName, Integer foodId, int score, double kcal, double carbo, double protein, double fat, double sugar, double fiber, double transfat, double saturatedFat, LocalDate date) {
        this.dietId = dietId;
        this.userId = userId;
        this.mealType = mealType;
        this.foodName = foodName;
        this.foodId = foodId;
        this.score = score;
        this.kcal = kcal;
        this.carbo = carbo;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.fiber = fiber;
        this.transfat = transfat;
        this.saturatedFat = saturatedFat;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDietId() {
        return dietId;
    }

    public void setDietId(int dietId) {
        this.dietId = dietId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
