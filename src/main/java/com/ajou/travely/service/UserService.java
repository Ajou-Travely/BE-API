package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.UserResponseDto;
import com.ajou.travely.controller.user.dto.UserUpdateRequestDto;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = findUserById(userId);
        String profilePath = requestDto.getProfileImage() == null
            ? null
            : awsS3Service.uploadFile(requestDto.getProfileImage());

        user.update(requestDto.getName(),
            requestDto.getPhoneNumber(),
            requestDto.getMbti(),
            requestDto.getSex(),
            requestDto.getBirthday(),
            profilePath);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(() ->
                new RecordNotFoundException(
                    "해당 ID의 User가 존재하지 않습니다.",
                    ErrorCode.USER_NOT_FOUND
                ));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        return new UserResponseDto(findUserById(userId));
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



}
