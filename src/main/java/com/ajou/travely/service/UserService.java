package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.user.UserCreateRequestDto;
import com.ajou.travely.controller.travel.dto.user.UserResponseInfoDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseInfoDto createUser(UserCreateRequestDto userCreateRequestDto) {
        User newUser = userCreateRequestDto.toEntity();
        User user = userRepository.save(newUser);
        return new UserResponseInfoDto(user);
    }

    public List<UserResponseInfoDto> getAllUsers() {
        List<UserResponseInfoDto> users = new ArrayList<>();
        userRepository.findAll().forEach(u -> users.add(new UserResponseInfoDto(u)));
        return users;
    }
}
