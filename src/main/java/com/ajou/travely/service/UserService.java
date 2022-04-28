package com.ajou.travely.service;

import com.ajou.travely.controller.user.dto.UserCreateRequestDto;
import com.ajou.travely.controller.user.dto.UserResponseInfoDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository
                .findAll();
    }
}
