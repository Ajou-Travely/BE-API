package com.ajou.travely.controller.user;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.travel.dto.ResponseWithPagination;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.UserCreateRequestDto;
import com.ajou.travely.controller.user.dto.UserResponseInfoDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public List<UserResponseInfoDto> getAllUsers() {
        return userService.getAllUsers().stream().map(UserResponseInfoDto::new).collect(Collectors.toList());
    }

    @PostMapping("/signup")
    public UserResponseInfoDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.insertUser(userCreateRequestDto.toEntity());
        return new UserResponseInfoDto(user);
    }

    @GetMapping("/travels")
    public ResponseEntity<ResponseWithPagination<SimpleTravelResponseDto>> showTravelsByUser(
            @LoginUser SessionUser sessionUser,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
            ) {
        PageRequest pageRequest = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size);
        List<SimpleTravelResponseDto>travels =  userService.getTravelsByUser(sessionUser.getUserId(), pageRequest);
        return ResponseEntity.ok(new ResponseWithPagination<>(page, size, travels));
    }
}
