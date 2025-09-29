package it.exam.backoffice.board.entity;

import it.exam.backoffice.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name ="tb_notice")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;   // 글 번호 (PK)

    private String title;    
    private String contents; 
    private String writer;   

    private int viewCount;   

    @OneToMany(mappedBy="board", cascade = CascadeType.ALL, orphanRemoval= true)
    private Set<BoardFileEntity> fileList = new HashSet<>();

    public void addFiles(BoardFileEntity entity) {
        if(fileList == null) this.fileList = new HashSet<>();
        entity.setBoard(this);
        fileList.add(entity);
    }
}
