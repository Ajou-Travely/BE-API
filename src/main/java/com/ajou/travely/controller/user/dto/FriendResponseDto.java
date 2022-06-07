package com.ajou.travely.controller.user.dto;

import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.SimplePostResponseDto;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FriendResponseDto {
    private final String name;
    private final String email;
    private final String profilePath;
    private final List<PostResponseDto> posts;

    public FriendResponseDto(User entity, List<Post> posts) {
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.profilePath = entity.getProfilePath();
        this.posts = posts
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
