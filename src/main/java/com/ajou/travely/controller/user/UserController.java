package com.ajou.travely.controller.user;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.common.ResponseWithPagination;
import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.*;
import com.ajou.travely.domain.kakao.KakaoFriendsResponse;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.KakaoNotAuthenticationExcpetion;
import com.ajou.travely.service.KakaoApiService;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping("/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final KakaoApiService kakaoApiService;

    @GetMapping("")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDto> updateUser(
            @LoginUser SessionUser sessionUser,
            @Valid @ModelAttribute UserUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(userService.updateUser(sessionUser.getUserId(), requestDto));
    }

    @PostMapping("/signup")
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.insertUser(userCreateRequestDto.toEntity());
        return new UserResponseDto(user);
    }

    @GetMapping("/email")
    public ResponseEntity<Boolean> isEmailDuplicated(@RequestParam String email) {
        return ResponseEntity.ok(userService.isEmailDuplicated(email));
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserProfileResponseDto> showMyInfo(@LoginUser SessionUser sessionUser) {
        UserProfileResponseDto user = userService.getMyProfile(sessionUser.getUserId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/friends")
    public ResponseEntity<Page<SimpleUserInfoDto>> showFriends(@LoginUser SessionUser sessionUser,
                                                               @PageableDefault(
                                                                       sort = {"id"},
                                                                       direction = Sort.Direction.DESC
                                                               ) Pageable pageable) {
        return ResponseEntity.ok(userService.getFriends(sessionUser.getUserId(), pageable));
    }

    @GetMapping("/friends/giving-requests")
    public ResponseEntity<Page<SimpleUserInfoDto>> showGivingRequests(@LoginUser SessionUser sessionUser,
                                                                      @PageableDefault(
                                                                              sort = {"id"},
                                                                              direction = Sort.Direction.DESC
                                                                      ) Pageable pageable) {
        return ResponseEntity.ok(userService.getGivingRequests(sessionUser.getUserId(), pageable));
    }

    @GetMapping("/friends/given-requests")
    public ResponseEntity<Page<SimpleUserInfoDto>> showGivenRequests(@LoginUser SessionUser sessionUser,
                                                                     @PageableDefault(
                                                                             sort = {"id"},
                                                                             direction = Sort.Direction.DESC
                                                                     ) Pageable pageable) {
        return ResponseEntity.ok(userService.getGivenRequests(sessionUser.getUserId(), pageable));
    }

    @PostMapping("/friends/{targetEmail}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable String targetEmail,
                                                    @LoginUser SessionUser sessionUser) {
        userService.requestFollowing(sessionUser.getUserId(), targetEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/{targetId}")
    public ResponseEntity<Void> cancelFriend(@PathVariable Long targetId,
                                             @LoginUser SessionUser sessionUser) {
        userService.cancelFollowing(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/given-requests/{targetId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long targetId,
                                                    @LoginUser SessionUser sessionUser) {
        userService.acceptFriendRequest(targetId, sessionUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/given-requests/{targetId}")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long targetId,
                                                    @LoginUser SessionUser sessionUser) {
        userService.rejectFriendRequest(targetId, sessionUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/giving-requests/{targetId}")
    public ResponseEntity<Void> cancelFriendRequest(@PathVariable Long targetId,
                                                    @LoginUser SessionUser sessionUser) {
        userService.cancelRequest(sessionUser.getUserId(), targetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/kakao/friends")
    public ResponseEntity<KakaoFriendsResponse> getKakaoFriends(@LoginUser SessionUser sessionUser) {
        if (Objects.nonNull(sessionUser.getAccessToken())) {
            throw new KakaoNotAuthenticationExcpetion("카카오 계정 인증이 필요합니다.", ErrorCode.KAKAO_NOT_AUTHENTICATION);
        }
        return ResponseEntity.ok(kakaoApiService.getKakaoFriends(sessionUser.getAccessToken()));
    }

    @GetMapping("/friend/{targetId}")
    public ResponseEntity<FriendResponseDto> showFriendProfile(@PathVariable Long targetId) {
        return ResponseEntity.ok(userService.getFriend(targetId));
    }
}
