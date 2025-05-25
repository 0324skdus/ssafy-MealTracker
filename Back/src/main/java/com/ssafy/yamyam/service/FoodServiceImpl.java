package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodDao foodDao;

    @Autowired
    public FoodServiceImpl(FoodDao foodDao) {
        this.foodDao = foodDao;
    }

    @Override
    public List<Food> searchByName(String keyword) {
        return foodDao.searchByName(keyword);
    }

    // 직접 등록
    @Override
    public int registerManualFood(Food food) {
        return foodDao.insert(food);
    }
}