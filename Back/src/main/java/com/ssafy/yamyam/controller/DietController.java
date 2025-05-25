package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.DietRegisterRequest;
import com.ssafy.yamyam.dto.Food;
import com.ssafy.yamyam.dto.MonthlyDiet;
import com.ssafy.yamyam.service.DailyScoreService;
import com.ssafy.yamyam.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diet")
public class DietController {

    private final DietService dietService;

	private final FoodDao foodDao;
	@Autowired
	public DietController(DietService dietService, FoodDao foodDao) {
	    this.dietService = dietService;
	    this.foodDao = foodDao;
	}


	// 식단 단일 조회
	@GetMapping("/{id}")
	public ResponseEntity<Diet> getDiet(@PathVariable("id") int id) {
		Diet diet = dietService.getById(id);
		if (diet != null)
			return ResponseEntity.ok(diet);
		return ResponseEntity.notFound().build();
	}

    // 사용자 전체 식단 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Diet>> getByUser(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(dietService.getByUserId(userId));
    }

    // 식단 수정
    @PutMapping
    public ResponseEntity<String> modify(@RequestBody Diet diet) {
        boolean result = dietService.modify(diet);
        if (result)
            return ResponseEntity.ok("식단 수정 성공");
        return ResponseEntity.badRequest().body("식단 수정 실패");
    }

    // 식단 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") int id) {
		boolean result = dietService.remove(id);
		if (result) {
			return ResponseEntity.ok("삭제 완료");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    // 날짜별 식단 조회
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<Diet>> getByUserAndDate(@PathVariable("userId") String userId,
                                                       @RequestParam("date") String dateStr) {

        LocalDate date = LocalDate.parse(dateStr);
        List<Diet> meals = dietService.getByUserIdAndDate(userId, date);
        return ResponseEntity.ok(meals);
    }


	@PostMapping("/by-food")
	public ResponseEntity<String> registerByFoodId(@RequestBody DietRegisterRequest dietRegisterRequest) {
	    try {
	    	boolean result = dietService.registerByFoodId(
	                dietRegisterRequest.getUserId(),
	                dietRegisterRequest.getMealType(),
	                dietRegisterRequest.getFoodId(),
	                dietRegisterRequest.getDate(),
	                dietRegisterRequest.getWeight()
	            );
	        if (result) return ResponseEntity.ok("식단 등록 성공");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return ResponseEntity.badRequest().body("식단 등록 실패");
	}

	
	// 한 달치 식단 요약 (점수 + 칼로리)
	@GetMapping("/summary/monthly")
	public ResponseEntity<List<MonthlyDiet>> getMonthlySummary(
	    @RequestParam("userId") String userId,
	    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	    List<MonthlyDiet> result = dietService.getMonthlySummary(userId, startDate, endDate);
	    return ResponseEntity.ok(result);
	}



}
