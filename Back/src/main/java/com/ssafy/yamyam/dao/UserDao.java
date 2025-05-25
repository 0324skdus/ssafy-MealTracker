package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.User;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserDao {

    // ID로 사용자 조회
    User selectById(String userId);

    // 전체 사용자 조회
    List<User> selectAll();

    // 사용자 등록
    int insert(User user);

    // 사용자 정보 수정
    int update(User user);

    // 사용자 삭제
    int delete(String userId);

    // 로그인 기능

    User login(@Param("userId") String userId, @Param("userPassword") String userPassword);
    //아이디찾기용(이름,이메일)
    String findUserIdByNameAndEmail(@Param("name") String name, @Param("email") String email);
    //비번찾기용(이름,아이디,이메일)
    User findUserByAllInfo(@Param("userId") String userId, @Param("name") String name, @Param("email") String email);
    void updatePassword(@Param("userId") String userId, @Param("password") String password);
    void updateName(@Param("userId") String userId, @Param("name") String name);
    void updateEmail(@Param("userId") String userId, @Param("email") String email);
    void updateUserId(@Param("userId") String userId, @Param("newUserId") String newUserId);

    int countByUserId(String userId);
}
