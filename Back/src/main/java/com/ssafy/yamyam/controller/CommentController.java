package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.dto.Comment;
import com.ssafy.yamyam.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Comment comment) {
        boolean result = commentService.register(comment);
        if (result) return ResponseEntity.ok("댓글 등록 성공");
        return ResponseEntity.badRequest().body("댓글 등록 실패");
    }

    @GetMapping("/post/{communityId}")
    public ResponseEntity<List<Comment>> getByCommunity(@PathVariable int communityId) {
        return ResponseEntity.ok(commentService.getByCommunityId(communityId));
    }

    @PutMapping
    public ResponseEntity<String> modify(@RequestBody Comment comment) {
        boolean result = commentService.modify(comment);
        if (result) return ResponseEntity.ok("댓글 수정 성공");
        return ResponseEntity.badRequest().body("댓글 수정 실패");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean result = commentService.remove(id);
        if (result) return ResponseEntity.ok("삭제 완료");
        return ResponseEntity.notFound().build();
    }
}
