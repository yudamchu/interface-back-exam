package it.exam.backoffice.board.controller;


import it.exam.backoffice.board.dto.BoardDTO;
import it.exam.backoffice.board.service.BoardService;
import it.exam.backoffice.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBoardList(
            @PageableDefault(size = 10, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(boardService.getBoardList(pageable)));
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<ApiResponse<BoardDTO.Detail>> getBoard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.ok(boardService.getBoard(id)));
    }

    @PostMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>> writeBoard(@ModelAttribute BoardDTO.Request dto) throws Exception {
        return ResponseEntity.ok(ApiResponse.ok(boardService.writeBoard(dto)));
    }
    

    @PutMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateBoard(
        @ModelAttribute BoardDTO.Request dto) throws Exception {
            return ResponseEntity.ok(ApiResponse.ok(boardService.updateBoard(dto)));
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteBoard(
        @PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(ApiResponse.ok(boardService.deleteBoard(id)));
    }

    @DeleteMapping("/board/file/{fileId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteFile(
        @PathVariable("fileId") Long fileId) throws Exception {
        return ResponseEntity.ok(ApiResponse.ok(boardService.deleteFile(fileId)));
    }
}
