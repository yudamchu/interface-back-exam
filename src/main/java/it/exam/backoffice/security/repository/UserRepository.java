package it.exam.backoffice.security.repository;

import it.exam.backoffice.security.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>{


    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAll(Pageable pageable);

    
    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAll(Specification<UserEntity> userSearchSpecification, Pageable pageable);


    
}
