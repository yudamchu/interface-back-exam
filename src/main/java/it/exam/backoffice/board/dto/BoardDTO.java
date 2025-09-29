package it.exam.backoffice.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.exam.backoffice.board.entity.BoardEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class BoardDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private Long noticeId;
        private String title;
        private String writer;
        private int viewCount;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regDate;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime modDate;

        public static Response of(BoardEntity entity) {
            return Response.builder()
                    .noticeId(entity.getNoticeId())
                    .title(entity.getTitle())
                    .writer(entity.getWriter())
                    .viewCount(entity.getViewCount())
                    .regDate(entity.getCreateDate())
                    .modDate(entity.getUpdateDate())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Detail {
        private Long noticeId;
        private String title;
        private String writer;
        private String contents;
        private int viewCount;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private List<BoardFileDTO> fileList;

        public static Detail of(BoardEntity entity) {
            List<BoardFileDTO> fileList =
                    entity.getFileList().stream().map(BoardFileDTO::of).toList();

            return Detail.builder()
                    .noticeId(entity.getNoticeId())
                    .title(entity.getTitle())
                    .writer(entity.getWriter())
                    .contents(entity.getContents())
                    .viewCount(entity.getViewCount())
                    .regDate(entity.getCreateDate())
                    .modDate(entity.getUpdateDate())
                    .fileList(fileList)
                    .build();
        }
    }

    @Data
    public static class Request {
        private Long noticeId;

        @NotBlank(message = "제목은 필수 항목입니다.")
        private String title;
        @NotBlank(message = "내용은 필수 항목입니다.")
        private String contents;
        private String writer;

        private MultipartFile file;
    }
}
