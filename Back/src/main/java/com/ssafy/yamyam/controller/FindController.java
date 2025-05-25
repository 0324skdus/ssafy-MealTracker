package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/find")
public class FindController {
//아이디/비번찾기용 임시컨트롤러, 겉보기 기능만 구현, 나중에 보완필요
    @Autowired
    private UserService userService;

    @PostMapping("/id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String userId = userService.findUserId(name, email);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 정보가 없습니다.");
    }

    @PostMapping("/password")
    public ResponseEntity<String> findPassword(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String userId = request.get("userId");
        String password = userService.resetPassword(userId, name, email);
        if (password != null) {
            return ResponseEntity.ok(password);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 정보가 없습니다.");
    }
}
