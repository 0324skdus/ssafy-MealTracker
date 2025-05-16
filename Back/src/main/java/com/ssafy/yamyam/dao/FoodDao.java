package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.Food;
import java.util.List;

public interface FoodDao {
    Food selectById(int foodId);
    List<Food> selectAll();
    int insert(Food food);
}
