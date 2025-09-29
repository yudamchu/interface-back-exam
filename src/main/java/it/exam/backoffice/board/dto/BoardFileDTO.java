package it.exam.backoffice.board.dto;

import it.exam.backoffice.board.entity.BoardFileEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardFileDTO {

    private Long fileId;
    private String fileName;
    private String storedName;
    private String filePath;
    private Long fileSize;
    private LocalDateTime regDate;

    public static BoardFileDTO of(BoardFileEntity entity) {
        return BoardFileDTO.builder()
                .fileId(entity.getFileId())
                .fileName(entity.getFileName())
                .storedName(entity.getStoredName())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .regDate(entity.getRegDate())
                .build();
    }
}
