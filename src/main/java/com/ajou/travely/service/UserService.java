package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.*;
import com.ajou.travely.domain.Friend;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.FriendRepository;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final PostRepository postRepository;

    private final FriendRepository friendRepository;

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = findUserById(userId);
        user.update(requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getMbti(),
                requestDto.getSex(),
                requestDto.getBirthday()
        );
        return new UserResponseDto(user);
    }

    public String updateUserAvatar(Long userId, MultipartFile avatar) {
        User user = findUserById(userId);
        String profilePath = avatar == null
                ? null
                : awsS3Service.uploadFile(avatar);
        user.updateAvatar(profilePath);
        return profilePath;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDto::new);
    }

    public User findUserById(Long userId) {
        return checkUserRecord(userId);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = checkUserRecord(userId);
        List<Post> posts = postRepository.findAllByUser(user);
        return new UserProfileResponseDto(user, posts);
    }

    @Transactional(readOnly = true)
    public Page<SimpleUserInfoDto> getFriends(Long userId, Pageable pageable) {
        User user = checkUserRecord(userId);
        return friendRepository
                .findAllFriendsByFollowee(user.getId(), pageable)
                .map(Friend::getFollower)
                .map(SimpleUserInfoDto::new);
    }

    @Transactional(readOnly = true)
    public Page<SimpleTravelResponseDto> getTravelsByUser(Long userId, Pageable pageable) {
        return userRepository
            .findTravelsByUserId(userId, pageable)
            .map(SimpleTravelResponseDto::new);
    }

    public Page<SimpleUserInfoDto> getGivenRequests(Long userId, Pageable pageable) {
        User user = checkUserRecord(userId);
        return friendRepository
                .findGivenRequestsByFollower(user.getId(), pageable)
                .map(Friend::getFollowee)
                .map(SimpleUserInfoDto::new);
    }

    @Transactional(readOnly = true)
    public Page<SimpleUserInfoDto> getGivingRequests(Long userId, Pageable pageable) {
        User user = checkUserRecord(userId);
        return friendRepository
                .findGivingRequestsByFollowee(user.getId(), pageable)
                .map(Friend::getFollower)
                .map(SimpleUserInfoDto::new);
    }

    @Transactional
    public void acceptFriendRequest(Long userId, Long targetId) {
        User user = checkUserRecord(userId);
        User target = checkUserRecord(targetId);
        Friend friend = checkFriendRecord(user, target);
        friend.acceptFriendRequest();
        friendRepository.save(new Friend(target, user, true));
    }

    @Transactional
    public void rejectFriendRequest(Long userId, Long targetId) {
        User user = checkUserRecord(userId);
        User target = checkUserRecord(targetId);
        Friend friend = checkFriendRecord(user, target);
        friendRepository.delete(friend);
    }

    @Transactional
    public SimpleUserInfoDto requestFollowing(Long userId, String targetEmail) {
        User user = checkUserRecord(userId);
        User target = checkUserRecordByEmail(targetEmail);
        checkDuplicatedRequest(user, target, false);
        checkDuplicatedRequest(target, user, true);
        friendRepository.save(new Friend(user, target));
        return new SimpleUserInfoDto(target);
    }

    @Transactional
    public void cancelRequest(Long userId, Long targetId) {
        User user = checkUserRecord(userId);
        User target = checkUserRecord(targetId);
        Friend friend = checkFriendRecord(user, target);
        if (friend.getIsFriend()) {
            throw new DuplicatedRequestException(
                    "해당 user와 이미 친구 상태입니다.",
                    ErrorCode.ALREADY_FRIEND
            );
        }
        friendRepository.delete(friend);
    }

    @Transactional
    public void cancelFollowing(Long userId, Long targetId) {
        User user = checkUserRecord(userId);
        User target = checkUserRecord(targetId);
        friendRepository.deleteByFolloweeIdAndFollowerId(user.getId(), target.getId());
        friendRepository.deleteByFolloweeIdAndFollowerId(target.getId(), user.getId());
    }

    @Transactional(readOnly = true)
    public Boolean isEmailDuplicated(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }

    @Transactional
    public FriendResponseDto getFriend(Long targetId) {
        User target = checkUserRecord(targetId);
        List<Post> posts = postRepository.findAllByUser(target);
        return new FriendResponseDto(target, posts);
    }

    private User checkUserRecord(Long userId) {
        return checkRecord(
                userRepository.findById(userId),
                "해당 ID의 User가 존재하지 않습니다.",
                ErrorCode.USER_NOT_FOUND
        );
    }

    private User checkUserRecordByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 Email의 User가 존재하지 않습니다.",
                        ErrorCode.USER_NOT_FOUND
                        )
                );
    }

    private Friend checkFriendRecord(User user, User target) {
        return checkRecord(
                friendRepository.findByFolloweeAndFollower(user,target),
                "해당 친구 요청이 존재하지 않습니다.",
                ErrorCode.FRIEND_NOT_FOUND
        );
    }

    private void checkDuplicatedRequest(User user, User target, Boolean isReverse) {
        String msg = isReverse
                ? "해당 user로부터 이미 친구 요청이 와있습니다."
                : "해당 user에게 이미 친구 요청을 보냈습니다.";
        Optional<Friend> isExcept = friendRepository.findByFolloweeAndFollower(user, target);
        if (isExcept.isPresent()) {
            if (isExcept.get().getIsFriend()) {
                throw new DuplicatedRequestException(
                        "해당 user와 이미 친구 상태입니다.",
                        ErrorCode.ALREADY_FRIEND
                );
            } else {
                throw new DuplicatedRequestException(
                        msg,
                        ErrorCode.ALREADY_REQUESTED
                );
            }
        }
    }

    private <T> T checkRecord(Optional<T> record, String message, ErrorCode code) {
        return record.orElseThrow(() ->
                new RecordNotFoundException(message, code));
    }
}
