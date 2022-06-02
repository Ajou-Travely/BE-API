package com.ajou.travely.service;

import com.ajou.travely.domain.user.CustomUserDetails;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUserId(Long userId, String accessToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("해당 사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user, accessToken);
    }
}
