package it.exam.backoffice.admin.controller;

import it.exam.backoffice.security.entity.UserEntity;
import it.exam.backoffice.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserRepository userRepository;

    // 관리자만 회원 리스트 조회 가능
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserEntity> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
