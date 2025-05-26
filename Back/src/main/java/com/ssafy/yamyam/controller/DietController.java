package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dao.DailyScoreDao;
import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.Diet;
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
	public ResponseEntity<?> registerByFood(@RequestBody Diet diet) {
		if (diet.getFoodId() != null) {
			// 기존 음식 등록 흐름 (foodId 존재)
			Food food = foodDao.selectById(diet.getFoodId());
			if (food == null) {
				return ResponseEntity.badRequest().body("존재하지 않는 foodId입니다.");
			}

			double factor = (diet.getWeight() > 0) ? diet.getWeight() / 100.0 : 1.0;

			diet.setFoodName(food.getFoodName());
			diet.setKcal(food.getKcal() * factor);
			diet.setCarbo(food.getCarbo() * factor);
			diet.setProtein(food.getProtein() * factor);
			diet.setFat(food.getFat() * factor);
			diet.setSugar(food.getSugar() * factor);
			diet.setFiber(food.getFiber() * factor);
			diet.setTransfat(food.getTransfat() * factor);
			diet.setSaturatedFat(food.getSaturatedFat() * factor);
		}

		// foodId가 null이어도 이 메서드로 통일해서 처리 가능
		dietService.register(diet);  // 이쪽에서 insert
		return ResponseEntity.ok().build();
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
