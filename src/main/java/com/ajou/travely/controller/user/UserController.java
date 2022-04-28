package com.ajou.travely.controller.user;

import com.ajou.travely.controller.user.dto.UserCreateRequestDto;
import com.ajou.travely.controller.user.dto.UserResponseInfoDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/users")
    public List<UserResponseInfoDto> getAllUsers() {
        return userService.getAllUsers().stream().map(UserResponseInfoDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/v1/users")
    public UserResponseInfoDto createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.insertUser(userCreateRequestDto.toEntity());
        return new UserResponseInfoDto(user);
    }
}
