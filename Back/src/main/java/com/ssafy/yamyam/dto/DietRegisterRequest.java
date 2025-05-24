package com.ssafy.yamyam.dto;

import java.time.LocalDate;

public class DietRegisterRequest {
    private String userId;
    private String mealType;
    private int foodId;
    private LocalDate date;
    private double weight; // 계산용
}

