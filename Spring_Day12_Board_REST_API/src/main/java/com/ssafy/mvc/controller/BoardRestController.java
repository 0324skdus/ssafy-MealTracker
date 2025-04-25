package com.ssafy.mvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mvc.model.dto.Board;
import com.ssafy.mvc.model.dto.SearchCondition;
import com.ssafy.mvc.model.service.BoardService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Board RESTful API", description = "게시판 CRU를 할수있음")
public class BoardRestController {

	// 서비스 의존성 주입이 필요하다.
	private final BoardService boardService;
	// 생성자 주입
	public BoardRestController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	
	//게시글 전체 조회
//	@GetMapping("/board")
//	public ResponseEntity<List<Board>> list(){
//		List<Board> list = boardService.getBoardList();
////		return new ResponseEntity<>(list, HttpStatus.OK);
//		return ResponseEntity.ok(list);
//	}
//	
	//검색
	@GetMapping("/board")
	@Operation(summary = "게시글 조회, 검색을 곁들인", description = "조건에 따른 검색을 수행할 수 있다.")
	public ResponseEntity<?> list(@ModelAttribute SearchCondition condition){
		System.out.println(condition);
		List<Board> list = boardService.search(condition);
		
		if(list == null || list.size() == 0) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<List<Board>>(list, HttpStatus.OK);
	}
	
	//게시글 상세보기
	@GetMapping("/board/{id}")
	public ResponseEntity<Board> detail(@PathVariable("id") int id){
		Board board = boardService.readBoard(id);
		if(board != null) {
			return ResponseEntity.ok(board);
		}
		//board 가 null인 상황 -> URL 직접 입력해서 들어왔다!
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	//게시글 등록(Form 데이터)
	@PostMapping("/board")
	public ResponseEntity<Void> write(@ModelAttribute Board board){
		System.out.println(board);
		boardService.writeBoard(board);
		System.out.println(board);
		
		//id는 알게됬어! -> 어떻게 넘기지
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	//게시글 삭제
	@Hidden
	@DeleteMapping("/board/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") int id){
		boolean isDeleted = boardService.removeBoard(id);
		
		if(isDeleted)
			return ResponseEntity.status(HttpStatus.OK).body("Board delete");
			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
	}
	
	//게시글 수정(JSON 형식)
//	@PutMapping("/board")
	@PutMapping("/board/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody Board board ){
		board.setId(id);
		boardService.modifyBoard(board);
		
		//아주 심플하게 OK를 넘겼지만..
		
		//사실은 ID에 해당되는 보드가 없을 수도 있고
		//데이터가 다 넘어오지 않아서 이슈가 생길 수도 있고 
		//분기처리는 사실 많이 하려면 할 수 있따.!!
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	
	
	
	
}
