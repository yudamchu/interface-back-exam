package it.exam.backoffice.board.repository;

import it.exam.backoffice.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @EntityGraph(attributePaths = {"fileList"})
    Page<BoardEntity> findAll(Pageable pageable);

    @Query("select b from BoardEntity b left join fetch b.fileList where b.noticeId = :noticeId")
    Optional<BoardEntity> getBoard(@Param("noticeId") Long noticeId);
}
