package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.Food;

import java.util.List;

public interface FoodService {
    List<Food> searchByName(String keyword);
}
