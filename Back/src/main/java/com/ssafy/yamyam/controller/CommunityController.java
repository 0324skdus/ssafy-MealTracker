package com.ssafy.yamyam.controller;

import com.ssafy.yamyam.config.jwt.CustomUserDetails;
import com.ssafy.yamyam.dto.Community;
import com.ssafy.yamyam.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
		// 인증 체크용 출력코드, 제출 전 삭제할것
		System.out.println("[컨트롤러] loginUser: " + loginUser);
		System.out.println("[컨트롤러] SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
		}

		community.setUserId(loginUser.getUserId());

		boolean result = communityService.register(community);
		if (result)
			return ResponseEntity.ok("게시글 등록 성공");
		return ResponseEntity.badRequest().body("게시글 등록 실패");
	}

//게시글 단일조회
	@GetMapping("/{id}")
	public ResponseEntity<Community> get(@PathVariable("id") int id) {
		// 체크용 출력코드, 제출전 삭제할것
		System.out.println("[게시글 단일 조회] Authentication: " + SecurityContextHolder.getContext().getAuthentication());
		Community community = communityService.getById(id);
		if (community != null)
			return ResponseEntity.ok(community);
		return ResponseEntity.notFound().build();
	}

//게시글 전체조회
	@GetMapping
	public ResponseEntity<List<Community>> getAll() {
		return ResponseEntity.ok(communityService.getAll());
	}

	// 게시글 수정
	@PutMapping("/{id}")
	public ResponseEntity<String> modify(@PathVariable("id") int id, @RequestBody Community community,
			@AuthenticationPrincipal CustomUserDetails loginUser) {
		// 체크용 출력코드, 제출전 삭제할것
		System.out.println("[컨트롤러] loginUser: " + loginUser);
		System.out.println("[컨트롤러] SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

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
		if (result)
			return ResponseEntity.ok("게시글 수정 성공");
		return ResponseEntity.badRequest().body("게시글 수정 실패");
	}

//게시글 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") int id,
			@AuthenticationPrincipal CustomUserDetails loginUser) {
		// 체크용 출력코드, 제출전 삭제할것
		System.out.println("[컨트롤러] loginUser: " + loginUser);
		System.out.println("[컨트롤러] SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

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
		if (result)
			return ResponseEntity.ok("삭제 완료");
		return ResponseEntity.badRequest().body("삭제 실패");
	}

//좋아요 달기
	@PostMapping("/{id}/like")
	public ResponseEntity<?> likeCommunity(@PathVariable("id") int communityId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 체크용 출력코드, 제출전 삭제할것
		System.out.println("[컨트롤러] userDetails: " + userDetails);
		System.out.println("[컨트롤러] SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
		}

		String userId = userDetails.getUserId();

		if (communityService.hasUserLiked(communityId, userId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 추천한 게시글입니다.");
		}

		communityService.addLike(communityId, userId);
		return ResponseEntity.ok("추천 완료");
	}

	// 게시판 조회
	@GetMapping("/board")
	public List<Community> getBoardList() {
		return communityService.getBoardList();
	}
}
