package com.ajou.travely.service;

import com.ajou.travely.controller.post.dto.PostCreateRequestDto;
import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.PostUpdateRequestDto;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    private final PhotoService photoService;

    public Long createPost(Long userId, PostCreateRequestDto requestDto) {
        User user = findUserById(userId);
        Schedule schedule = findScheduleById(requestDto.getScheduleId());
        Post post = Post.builder()
            .schedule(schedule)
            .user(user)
            .text(requestDto.getText())
            .title(requestDto.getTitle())
            .build();

        if (!requestDto.getPhotos().isEmpty()) {
            photoService.createPhotos(post, requestDto.getPhotos());
        }

        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(initializePostInfo(postId));
    }

    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = findPostById(postId);
        post.update(requestDto.getTitle(), requestDto.getText());

        if (requestDto.getAddPhotos() != null) {
            photoService.createPhotos(post, requestDto.getAddPhotos());
        }
        if (requestDto.getRemovePhotoIds() != null) {
            photoService.removePhotoIds(requestDto.getRemovePhotoIds());
        }
    }

    public void deletePost(Long postId){
        photoService.removePhotos(findPostById(postId).getPhotos());
        postRepository.deleteById(postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: id=" + postId));
    }

    @Transactional(readOnly = true)
    public Post initializePostInfo(Long postId) {
        Post post = findPostById(postId);
        Hibernate.initialize(post.getPhotos());
        Hibernate.initialize(post.getComments());
        return post;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: id=" + userId));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Invalid Schedule ID: id=" + scheduleId));
    }

}
