package com.ssafy.yamyam.dto;

import java.time.LocalDate;

public class DietRegisterRequest {
    private String userId;
    private String mealType;
    private Integer foodId;
    private LocalDate date;
    private double weight;

    public DietRegisterRequest(String userId, String mealType, Integer foodId, LocalDate date, double weight) {
        this.userId = userId;
        this.mealType = mealType;
        this.foodId = foodId;
        this.date = date;
        this.weight = weight;
    }

    public DietRegisterRequest() {
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

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
