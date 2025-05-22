package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.Food;
import com.ssafy.yamyam.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFoodByName(@RequestParam("keyword") String keyword) {
        List<Food> foods = foodService.searchByName(keyword);
        return ResponseEntity.ok(foods);
    }
}