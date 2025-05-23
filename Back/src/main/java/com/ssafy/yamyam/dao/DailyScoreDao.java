package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.DailyScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DailyScoreDao {
    void insert(DailyScore dailyScore);

    void update(DailyScore dailyScore);

    DailyScore selectByUserIdAndDate(Map<String, Object> map);

    List<DailyScore> selectByUserId(String userId);

    List<DailyScore> selectScoresByUserAndPeriod(@Param("userId") String userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    void deleteByUserAndDate(@Param("userId") String userId, @Param("date") LocalDate date);

}
