package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.Diet;

import java.time.LocalDate;
import java.util.List;

public interface DietService {

    // 식단 등록
    boolean register(Diet diet);

    // 식단 단일 조회
    Diet getById(int dietId);

    // 유저별 식단 전체 조회
    List<Diet> getByUserId(String userId);
    
    // 특정 날짜 식단 조회
    List<Diet> getByUserIdAndDate(String userId, LocalDate date);

    // 식단 수정
    boolean modify(Diet diet);

    // 식단 삭제
    boolean remove(int id);
    
    // 식단 점수 채점? 주기?
    int scoring(String userId,LocalDate date);

    public boolean registerByFoodId(String userId, String mealType, int foodId, LocalDate date);
}
