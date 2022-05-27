package com.ajou.travely.controller.user;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.common.ResponseWithPagination;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
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

    @GetMapping("/friends")
    public ResponseEntity<Page<SimpleUserInfoDto>> showFriends(@LoginUser SessionUser sessionUser,
                                                               @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getFriends(sessionUser.getUserId(), pageable));
    }

    @GetMapping("/friends/giving-requests")
    public ResponseEntity<Page<SimpleUserInfoDto>> showGivingRequests(@LoginUser SessionUser sessionUser,
                                                                      @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getGivingRequests(sessionUser.getUserId(), pageable));
    }

    @GetMapping("/friends/given-requests")
    public ResponseEntity<Page<SimpleUserInfoDto>> showGivenRequests(@LoginUser SessionUser sessionUser,
                                                                     @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getGivenRequests(sessionUser.getUserId(), pageable));
    }

    @PostMapping("/friends/{targetId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long targetId,
                                                  @LoginUser SessionUser sessionUser) {
        userService.requestFollowing(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/{targetId}")
    public ResponseEntity<Void> cancelFriend(@PathVariable Long targetId,
                                             @LoginUser SessionUser sessionUser) {
        userService.cancelFollowing(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/request/{targetId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long targetId,
                                                    @LoginUser SessionUser sessionUser) {
        userService.acceptFriendRequest(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/request/{targetId}")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long targetId,
                                                    @LoginUser SessionUser sessionUser) {
        userService.rejectFriendRequest(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }
}
