package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.config.jwt.CustomUserDetails;
import com.ssafy.yamyam.dto.Community;
import com.ssafy.yamyam.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Community community,
                                           @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        community.setUserId(loginUser.getUserId());
        boolean result = communityService.register(community);
        return result ?
                ResponseEntity.ok("게시글 등록 성공") :
                ResponseEntity.badRequest().body("게시글 등록 실패");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> get(@PathVariable("id") int id) {
        Community community = communityService.getById(id);
        return community != null ?
                ResponseEntity.ok(community) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Community>> getAll(@RequestParam("sort") String sort) {
        List<Community> list;
        switch (sort) {
            case "인기글":
                list = communityService.getByLikeCount();
                break;
            case "최신글":
                list = communityService.getByCreatedAt();
                break;
            case "식단":
                list = communityService.getByType("식단 게시판");
                break;
            case "자유게시판":
                list = communityService.getByType("자유 게시판");
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modify(@PathVariable("id") int id, @RequestBody Community community,
                                         @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }

        Community original = communityService.getById(id);
        if (original == null) {
            return ResponseEntity.notFound().build();
        }

        if (!original.getUserId().equals(loginUser.getUserId())) {
            return ResponseEntity.status(403).body("작성자만 수정할 수 있습니다");
        }

        community.setId(id);
        community.setUserId(loginUser.getUserId());

        boolean result = communityService.modify(community);
        return result ?
                ResponseEntity.ok("게시글 수정 성공") :
                ResponseEntity.badRequest().body("게시글 수정 실패");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id,
                                         @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }

        Community original = communityService.getById(id);
        if (original == null) {
            return ResponseEntity.notFound().build();
        }

        if (!original.getUserId().equals(loginUser.getUserId())) {
            return ResponseEntity.status(403).body("작성자만 삭제할 수 있습니다");
        }

        boolean result = communityService.remove(id);
        return result ?
                ResponseEntity.ok("삭제 완료") :
                ResponseEntity.badRequest().body("삭제 실패");
    }

    @GetMapping("/board")
    public List<Community> getBoardList() {
        return communityService.getBoardList();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("id") int communityId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }

        String userId = userDetails.getUserId();
        boolean liked = communityService.toggleLike(communityId, userId);
        return ResponseEntity.ok(liked ? "좋아요 등록" : "좋아요 취소");
    }
}
