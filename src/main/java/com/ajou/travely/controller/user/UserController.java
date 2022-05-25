package com.ajou.travely.controller.user;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.common.ResponseWithPagination;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.UserCreateRequestDto;
import com.ajou.travely.controller.user.dto.UserResponseDto;
import com.ajou.travely.controller.user.dto.UserUpdateRequestDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
            .map(UserResponseDto::new)
            .collect(Collectors.toList());
    }

    @PutMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUser(
            @LoginUser SessionUser sessionUser,
            @Valid @ModelAttribute UserUpdateRequestDto requestDto
    ) {
        userService.updateUser(sessionUser.getUserId(), requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.insertUser(userCreateRequestDto.toEntity());
        return new UserResponseDto(user);
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserResponseDto> showMyInfo(@LoginUser SessionUser sessionUser) {
        UserResponseDto user = userService.getUserById(sessionUser.getUserId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/travels")
    public ResponseEntity<Page<SimpleTravelResponseDto>> showTravelsByUser(
            @LoginUser SessionUser sessionUser,
            @PageableDefault(
                size = 12,
                sort = "id",
                direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SimpleTravelResponseDto> travels =  userService.getTravelsByUser(sessionUser.getUserId(), pageable);
        return ResponseEntity.ok(travels);
    }
}
