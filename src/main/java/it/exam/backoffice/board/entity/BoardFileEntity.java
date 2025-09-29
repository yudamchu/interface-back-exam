package it.exam.backoffice.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="tb_notice_files")
public class BoardFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;   // 파일 번호 (PK)

    private String fileName;    
    private String storedName;  
    private String filePath;    
    private Long fileSize;      

    @Column(updatable = false)
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "notice_id")   // FK 매핑
    private BoardEntity board;
}
