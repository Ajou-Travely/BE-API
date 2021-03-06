package com.ajou.travely.service;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.controller.user.dto.UserUpdateRequestDto;
import com.ajou.travely.domain.user.Mbti;
import com.ajou.travely.domain.user.Sex;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"

})
class UserServiceTest {
    @Autowired
    UserService userService;

//    EntityManager

    PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    void testGetUserInfo() {
        String email = "test@test";
        String phoneNumber = "119";
        String name = "119";
        LocalDate birthday = LocalDate.of(1998, 6, 3);
        User user = userService.insertUser(
                User.builder()
                        .kakaoId(0L)
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .name(name)
                        .userType(UserType.USER)
                        .birthday(birthday)
                        .build()
        );

        User findUser = userService.findUserById(user.getId());

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void testRequestFollowing() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());

        Page<SimpleUserInfoDto> givenRequests = userService.getGivenRequests(target.getId(), pageRequest);
        assertThat(givenRequests).hasSize(1);
        Page<SimpleUserInfoDto> givingRequests = userService.getGivingRequests(user.getId(), pageRequest);
        assertThat(givingRequests).hasSize(1);
    }

    @Test
    void testInvalidRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        assertThatThrownBy(() -> userService.requestFollowing(user.getId(), target.getEmail()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user?????? ?????? ?????? ????????? ???????????????.");
        assertThatThrownBy(() -> userService.requestFollowing(target.getId(), user.getEmail()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user????????? ?????? ?????? ????????? ???????????????.");
        userService.acceptFriendRequest(user.getId(), target.getId());
        assertThatThrownBy(() -> userService.requestFollowing(user.getId(), target.getEmail()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user??? ?????? ?????? ???????????????.");
        assertThatThrownBy(() -> userService.requestFollowing(target.getId(), user.getEmail()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user??? ?????? ?????? ???????????????.");
    }

    @Test
    void testCancelRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.cancelRequest(user.getId(), target.getId());

        Page<SimpleUserInfoDto> givenRequests = userService.getGivenRequests(target.getId(), pageRequest);
        assertThat(givenRequests).hasSize(0);
        Page<SimpleUserInfoDto> givingRequests = userService.getGivingRequests(user.getId(), pageRequest);
        assertThat(givingRequests).hasSize(0);
    }

    @Test
    void testInvalidCancelRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.cancelRequest(user.getId(), target.getId());
        assertThatThrownBy(() -> userService.cancelRequest(user.getId(), target.getId()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testDuplicatedCancelRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.acceptFriendRequest(user.getId(), target.getId());
        assertThatThrownBy(() -> userService.cancelRequest(user.getId(), target.getId()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user??? ?????? ?????? ???????????????.");
    }

    @Test
    void testRejectRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.rejectFriendRequest(user.getId(), target.getId());

        Page<SimpleUserInfoDto> givenRequests = userService.getGivenRequests(target.getId(), pageRequest);
        assertThat(givenRequests).hasSize(0);
        Page<SimpleUserInfoDto> givingRequests = userService.getGivingRequests(user.getId(), pageRequest);
        assertThat(givingRequests).hasSize(0);
    }

    @Test
    void testInvalidReject() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.rejectFriendRequest(user.getId(), target.getId());

        assertThatThrownBy(() -> userService.rejectFriendRequest(user.getId(), target.getId()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testAcceptRequest() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.acceptFriendRequest(user.getId(), target.getId());

        Page<SimpleUserInfoDto> friends = userService.getFriends(user.getId(), pageRequest);
        assertThat(friends).hasSize(1);
        Page<SimpleUserInfoDto> targetFriends = userService.getFriends(target.getId(), pageRequest);
        assertThat(targetFriends).hasSize(1);
    }

    @Test
    void testDuplicatedAccept() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        userService.requestFollowing(user.getId(), target.getEmail());
        userService.acceptFriendRequest(user.getId(), target.getId());

        assertThatThrownBy(() -> userService.acceptFriendRequest(user.getId(), target.getId()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user??? ?????? ?????? ???????????????.");
        assertThatThrownBy(() -> userService.acceptFriendRequest(target.getId(), user.getId()))
                .isInstanceOf(DuplicatedRequestException.class)
                .hasMessage("?????? user??? ?????? ?????? ???????????????.");
    }

    @Test
    void testInvalidAccept() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );

        assertThatThrownBy(() -> userService
                .acceptFriendRequest(user.getId(), target.getId()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testCancelFriend() {
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email("1@1")
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        User target = userService.insertUser(
                User.builder().kakaoId(1L)
                        .email("2@2")
                        .phoneNumber("222")
                        .name("kim")
                        .userType(UserType.USER)
                        .build()
        );
        userService.requestFollowing(user.getId(), target.getEmail());
        userService.acceptFriendRequest(user.getId(), target.getId());
        userService.cancelFollowing(user.getId(), target.getId());

        Page<SimpleUserInfoDto> friends = userService.getFriends(user.getId(), pageRequest);
        assertThat(friends).hasSize(0);
        Page<SimpleUserInfoDto> targetFriends = userService.getFriends(target.getId(), pageRequest);
        assertThat(targetFriends).hasSize(0);
    }

    @Test
    void testUpdateUser() {
        // given
        String email = "test@test";
        String phoneNumber = "119";
        String name = "119";
        LocalDate birthday = LocalDate.of(1998, 6, 3);
        User user = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .phoneNumber(phoneNumber)
                        .email(email)
                        .name(name)
                        .kakaoId(0L)
                        .build()
        );

        // when
        String updateName = "update name";
        String updatePhoneNumber = "010-1111-1111";
        LocalDate updateBirthday = LocalDate.of(2020, 6, 3);
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .name(updateName)
                .phoneNumber(updatePhoneNumber)
                .mbti(Mbti.ENFP)
                .sex(Sex.FEMALE)
                .birthday(updateBirthday)
                .build();

        userService.updateUser(user.getId(), requestDto);

        // then
        User updateUser = userService.findUserById(user.getId());
        System.out.println(updateUser.toString());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void testIsEmailDuplicated() {
        String duplicateEmail = "test@test.com";
        String validEmail = "test123@test.com";
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
                        .email(duplicateEmail)
                        .phoneNumber("111")
                        .name("park")
                        .userType(UserType.USER)
                        .build()
        );
        Assertions.assertThat(userService.isEmailDuplicated(duplicateEmail)).isEqualTo(false);
        Assertions.assertThat(userService.isEmailDuplicated(validEmail)).isEqualTo(true);
    }
}

