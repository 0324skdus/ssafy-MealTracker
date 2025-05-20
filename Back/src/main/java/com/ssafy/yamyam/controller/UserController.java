package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.User;
import com.ssafy.yamyam.config.jwt.JwtUtil;
import com.ssafy.yamyam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 사용자 전체 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 사용자 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자 등록
    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        System.out.println("[UserController] /api/user POST 요청 도착");
        System.out.println("[UserController] user 객체: " + user);

        if (user == null) {
            System.out.println("[UserController] user 객체가 null입니다.");
            return ResponseEntity.status(400).body("User 객체 바인딩 실패");
        }

        boolean result = userService.register(user);
        return result ?
                ResponseEntity.ok("회원가입 성공") :
                ResponseEntity.status(400).body("회원가입 실패");
    }

    // 사용자 정보 수정
    @PutMapping
    public ResponseEntity<String> update(@RequestBody User user) {
        boolean result = userService.modify(user);
        if (result) {
            return ResponseEntity.ok("회원 정보 수정 성공");
        } else {
            return ResponseEntity.status(400).body("수정 실패");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable("userId") String userId) {
        boolean result = userService.remove(userId);
        if (result) {
            return ResponseEntity.ok("회원 탈퇴 성공");
        } else {
            return ResponseEntity.status(400).body("회원 탈퇴 실패");
        }
    }




    @GetMapping("/check-id")
    public ResponseEntity<String> checkUserId(@RequestParam String userId) {
        boolean exists = userService.isUserIdExists(userId);
        if (exists) {
            return ResponseEntity.ok("존재하는 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }
}
