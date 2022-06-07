package com.ajou.travely.controller.user.dto;

import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.SimplePostResponseDto;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserProfileResponseDto {

    private final Long userId;

    private final String name;

    private final String email;

//    private final Sex sex;

    private final String phoneNumber;

//    private final LocalDate birthday;

    private final String profilePath;

    private final UserType userType;

    private final List<PostResponseDto> posts;

    public UserProfileResponseDto(User entity, List<Post> posts) {
        this.userId = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
//        this.sex = entity.getSex();
        this.phoneNumber = entity.getPhoneNumber();
        this.profilePath = entity.getProfilePath();
        this.userType = entity.getUserType();
//        this.birthday = entity.getBirthday();
        this.posts = posts
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
