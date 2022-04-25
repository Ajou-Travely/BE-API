package com.ajou.travely.controller.user;

import com.ajou.travely.controller.travel.dto.user.UserCreateRequestDto;
import com.ajou.travely.controller.travel.dto.user.UserResponseInfoDto;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/users")
    public List<UserResponseInfoDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/v1/users")
    public UserResponseInfoDto createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.createUser(userCreateRequestDto);
    }
}
