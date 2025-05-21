package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dao.FoodDao;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.Food;
import com.ssafy.yamyam.dto.MonthlyDiet;
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
        if (result)
            return ResponseEntity.ok("삭제 완료");
        return ResponseEntity.notFound().build();
    }

    // 날짜별 식단 조회
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<Diet>> getByUserAndDate(@PathVariable("userId") String userId,
                                                       @RequestParam("date") String dateStr) {

        LocalDate date = LocalDate.parse(dateStr);
        List<Diet> meals = dietService.getByUserIdAndDate(userId, date);
        return ResponseEntity.ok(meals);
    }

    // 식단 점수 조회
    @GetMapping("/user/{userId}/score")
    public ResponseEntity<Integer> getScore(@PathVariable("userId")  String userId, @RequestParam("date") String dateStr) {

        LocalDate date = LocalDate.parse(dateStr);
        int score = dietService.scoring(userId, date);
        return ResponseEntity.ok(score);
    }

	@PostMapping("/by-food")
	public ResponseEntity<String> registerByFoodId(@RequestBody Diet diet) {
	    try {
	        // 날짜 파싱
	        LocalDate parsedDate = diet.getDate();

	        // 식품 ID로 식품 정보 조회
	        Food food = foodDao.selectById(diet.getFoodId());
	        if (food == null) {
	            return ResponseEntity.badRequest().body("식품 정보 없음");
	        }

	        // weight 단위 기반 계산 (기본 100g)
	        double weight = (diet.getWeight() > 0) ? diet.getWeight() : 100.0;
	        double factor = weight / 100.0;

	        // 계산된 영양소 세팅
	        diet.setKcal(food.getKcal() * factor);
	        diet.setCarbo(food.getCarbo() * factor);
	        diet.setProtein(food.getProtein() * factor);
	        diet.setFat(food.getFat() * factor);
	        diet.setSaturatedFat(food.getSaturatedFat() * factor);
	        diet.setSugar(food.getSugar() * factor);
	        diet.setFiber(food.getFiber() * factor);
	        diet.setTransfat(food.getTransfat() * factor);

	        diet.setFoodName(food.getFoodName()); // 이름도 세팅
	        diet.setDate(parsedDate);

	        // 등록 실행
	        boolean result = dietService.register(diet);
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
