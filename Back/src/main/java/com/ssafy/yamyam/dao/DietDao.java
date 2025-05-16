package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.Diet;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DietDao {

    // 식단 등록
    int insert(Diet diet);

    // 식단 단일 조회
    Diet selectById(int dietId);

    // 전체 식단 조회
    List<Diet> selectByUserId(@Param("userId") String userId);
    
    // 사용자별 식단 특정 날짜 조회
    List<Diet> selectByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);
    
    // 식단 수정
    int update(Diet diet);

    // 식단 삭제
    int delete(int dietId);
}
