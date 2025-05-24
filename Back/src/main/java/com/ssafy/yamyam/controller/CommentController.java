package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.config.jwt.CustomUserDetails;
import com.ssafy.yamyam.dto.Comment;
import com.ssafy.yamyam.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 댓글 등록
    @PostMapping
    public ResponseEntity<String> register(@RequestBody Comment comment,
                                           @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        comment.setUserId(loginUser.getUserId());
        boolean result = commentService.register(comment);
        return result
                ? ResponseEntity.ok("댓글 등록 성공")
                : ResponseEntity.badRequest().body("댓글 등록 실패");
    }

    // 특정 게시글의 댓글 전체 조회
    @GetMapping("/post/{communityId}")
    public ResponseEntity<List<Comment>> getByCommunity(@PathVariable("communityId") int communityId) {
        return ResponseEntity.ok(commentService.getByCommunityId(communityId));
    }

    // 댓글 수정
    @PutMapping
    public ResponseEntity<String> modify(@RequestBody Comment comment,
                                         @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Comment original = commentService.findById(comment.getId());
        if (original == null) {
            return ResponseEntity.notFound().build();
        }

        if (!original.getUserId().equals(loginUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 수정할 수 있습니다.");
        }

        comment.setUserId(original.getUserId());
        boolean result = commentService.modify(comment);
        return result
                ? ResponseEntity.ok("댓글 수정 성공")
                : ResponseEntity.badRequest().body("댓글 수정 실패");
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id,
                                         @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Comment original = commentService.findById(id);
        if (original == null) {
            return ResponseEntity.notFound().build();
        }

        if (!original.getUserId().equals(loginUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 삭제할 수 있습니다.");
        }

        boolean result = commentService.remove(id);
        return result
                ? ResponseEntity.ok("삭제 완료")
                : ResponseEntity.badRequest().body("삭제 실패");
    }
}
