package it.exam.backoffice.board.service;

import it.exam.backoffice.board.dto.BoardDTO;
import it.exam.backoffice.board.dto.BoardFileDTO;
import it.exam.backoffice.board.entity.BoardEntity;
import it.exam.backoffice.board.entity.BoardFileEntity;
import it.exam.backoffice.board.repository.BoardFileRepository;
import it.exam.backoffice.board.repository.BoardRepository;
import it.exam.backoffice.common.utils.FileUtils;
import it.exam.backoffice.security.dto.UserSecureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final FileUtils fileUtils;

    @Value("${server.file.upload.path}")
    String uploadPath;

    @Transactional(readOnly = true)
    public Map<String, Object> getBoardList(Pageable pageable) {
        Page<BoardEntity> page = boardRepository.findAll(pageable);
        List<BoardDTO.Response> list = page.getContent().stream()
                .map(BoardDTO.Response::of).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotalElements());
        result.put("page", page.getNumber());
        result.put("content", list);
        return result;
    }

    @Transactional(readOnly = true)
    public BoardDTO.Detail getBoard(Long id) {
        BoardEntity entity = boardRepository.getBoard(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        entity.setViewCount(entity.getViewCount() + 1);
        return BoardDTO.Detail.of(entity);
    }

    @Transactional
public Map<String, Object> writeBoard(BoardDTO.Request dto) throws Exception {
    BoardEntity entity = new BoardEntity();
    entity.setTitle(dto.getTitle());
    entity.setContents(dto.getContents());

    // ✅ 기존: 프론트에서 넘어온 값 사용
    // entity.setWriter(dto.getWriter());

    // ✅ 수정: 로그인 사용자에서 가져오기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserSecureDTO user = (UserSecureDTO) auth.getPrincipal();
    entity.setWriter(user.getUserName());

    if (dto.getFile() != null && !dto.getFile().isEmpty()) {
        Map<String, Object> fileMap = fileUtils.uploadFiles(dto.getFile(), uploadPath);
        BoardFileEntity fileEntity = new BoardFileEntity();
        fileEntity.setFileName(fileMap.get("fileName").toString());
        fileEntity.setStoredName(fileMap.get("storedFileName").toString());
        fileEntity.setFilePath(fileMap.get("filePath").toString());
        fileEntity.setFileSize(dto.getFile().getSize());
        entity.addFiles(fileEntity);
    }

    boardRepository.save(entity);
    return Map.of("resultCode", 200, "resultMsg", "OK");
}

    @Transactional
    public Map<String, Object> updateBoard(BoardDTO.Request dto) throws Exception {
        BoardEntity entity = boardRepository.getBoard(dto.getNoticeId())
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        BoardDTO.Detail oldDetail = BoardDTO.Detail.of(entity);

        entity.setTitle(dto.getTitle());
        entity.setContents(dto.getContents());

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            entity.getFileList().clear();
            Map<String, Object> fileMap = fileUtils.uploadFiles(dto.getFile(), uploadPath);
            BoardFileEntity fileEntity = new BoardFileEntity();
            fileEntity.setFileName(fileMap.get("fileName").toString());
            fileEntity.setStoredName(fileMap.get("storedFileName").toString());
            fileEntity.setFilePath(fileMap.get("filePath").toString());
            fileEntity.setFileSize(dto.getFile().getSize());
            entity.addFiles(fileEntity);

            if (oldDetail.getFileList() != null) {
                for (BoardFileDTO file : oldDetail.getFileList()) {
                    fileUtils.deleteFile(uploadPath + file.getStoredName());
                }
            }
        }

        boardRepository.save(entity);
        return Map.of("resultCode", 200, "resultMsg", "OK");
    }

    @Transactional
    public Map<String, Object> deleteBoard(Long id) throws Exception {
      
      Map<String, Object> resultMap = new HashMap<>();

      BoardEntity entity = boardRepository.getBoard(id)
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

      BoardDTO.Detail oldDetail = BoardDTO.Detail.of(entity);

      // 게시글 삭제 (연관 파일도 같이 제거됨: orphanRemoval)
      boardRepository.delete(entity);

      // 실제 서버 파일 삭제
      if (oldDetail.getFileList() != null && !oldDetail.getFileList().isEmpty()) {
        
        for (BoardFileDTO file : oldDetail.getFileList()) {
            String oldFilePath = uploadPath + file.getStoredName();
            fileUtils.deleteFile(oldFilePath);   // throws Exception 처리
        }
    }

      resultMap.put("resultCode", 200);
      resultMap.put("resultMsg", "OK");
      return resultMap;
  }

    @Transactional
    public Map<String, Object> deleteFile(Long fileId) throws Exception {
      Map<String, Object> resultMap = new HashMap<>();

      BoardFileEntity fileEntity = boardFileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("파일 없음"));

      String oldFilePath = fileEntity.getFilePath() + fileEntity.getStoredName();
      fileUtils.deleteFile(oldFilePath);   // throws Exception 처리

      boardFileRepository.delete(fileEntity);

      resultMap.put("resultCode", 200);
      resultMap.put("resultMsg", "OK");
      return resultMap;
    }
  
}
