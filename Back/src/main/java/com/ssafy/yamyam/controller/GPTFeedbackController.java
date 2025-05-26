package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.service.GPTService;
import com.ssafy.yamyam.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/gpt")
public class GPTFeedbackController {

    private final GPTService gptService;
    private final JwtUtil jwtUtil;

    @Autowired
    public GPTFeedbackController(GPTService gptService, JwtUtil jwtUtil) {
        this.gptService = gptService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/feedback")
    public String getFeedback(
            @RequestParam(name = "date") String date,
            @RequestHeader(name = "Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.getUserId(token);
        LocalDate parsedDate = LocalDate.parse(date);
        return gptService.getFeedback(userId, parsedDate);
    }

}
