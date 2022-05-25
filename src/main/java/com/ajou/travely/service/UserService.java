package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.controller.user.dto.UserResponseDto;
import com.ajou.travely.domain.Friend;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.FriendRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final FriendRepository friendRepository;

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return checkRecord(userId);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<SimpleTravelResponseDto> getTravelsByUser(Long userId, Pageable pageable) {
        return userRepository
                .findTravelsByUserId(userId, pageable)
                .stream()
                .map(SimpleTravelResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        return new UserResponseDto(checkRecord(userId));
    }

    @Transactional(readOnly = true)
    public List<SimpleUserInfoDto> getFriends(Long userId) {
        User user = checkRecord(userId);
        return friendRepository
                .findAllFriendsByFollowee(user.getId())
                .stream()
                .map(Friend::getFollower)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SimpleUserInfoDto> getGivenRequests(Long userId) {
        User user = checkRecord(userId);
        return friendRepository
                .findGivenRequestsByFollower(user.getId())
                .stream()
                .map(Friend::getFollowee)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SimpleUserInfoDto> getGivingRequests(Long userId) {
        User user = checkRecord(userId);
        return friendRepository
                .findGivingRequestsByFollowee(user.getId())
                .stream()
                .map(Friend::getFollower)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptFriendRequest(Long userId, Long targetId) {
        User user = checkRecord(userId);
        User target = checkRecord(targetId);
        Friend friend = friendRepository.findByFolloweeAndFollower(user, target)
                .orElseThrow(() -> new RecordNotFoundException("해당 친구 요청이 존재하지 않습니다.", ErrorCode.FRIEND_NOT_FOUND));
        friend.acceptFriendRequest();
        friendRepository.save(new Friend(target, user, true));
    }

    @Transactional
    public void rejectFriendRequest(Long userId, Long targetId) {
        User user = checkRecord(userId);
        User target = checkRecord(targetId);
        Friend friend = friendRepository.findByFolloweeAndFollower(user, target)
                .orElseThrow(() -> new RecordNotFoundException("해당 친구 요청이 존재하지 않습니다.", ErrorCode.FRIEND_NOT_FOUND));
        friendRepository.delete(friend);
    }

    @Transactional
    public void requestFollowing(Long userId, Long targetId) {
        User user = checkRecord(userId);
        User target = checkRecord(targetId);
        checkDuplicatedRequest(user, target, false);
        checkDuplicatedRequest(target, user, true);
        friendRepository.save(new Friend(user, target));
    }

    @Transactional
    public void cancelFollowing(Long userId, Long targetId) {
        User user = checkRecord(userId);
        User target = checkRecord(targetId);
        friendRepository.deleteByFolloweeIdAndFollowerId(user.getId(), target.getId());
        friendRepository.deleteByFolloweeIdAndFollowerId(target.getId(), user.getId());
    }

    private User checkRecord(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                                "해당 ID의 User가 존재하지 않습니다."
                                , ErrorCode.USER_NOT_FOUND
                        )
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
}
