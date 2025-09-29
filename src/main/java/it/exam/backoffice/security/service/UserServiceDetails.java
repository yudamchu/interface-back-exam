package it.exam.backoffice.security.service;

import it.exam.backoffice.security.dto.UserSecureDTO;
import it.exam.backoffice.security.entity.UserEntity;
import it.exam.backoffice.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceDetails implements UserDetailsService{
  
    private final UserRepository userRepository;
  
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user =
            userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

        return new UserSecureDTO(user.getUserId(), user.getUserName(),
                        user.getPasswd(), user.getRole().getRoleId());
    }

}
