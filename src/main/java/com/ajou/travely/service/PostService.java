package com.ajou.travely.service;

import com.ajou.travely.controller.post.dto.PostCreateRequestDto;
import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.PostUpdateRequestDto;
import com.ajou.travely.domain.Photo;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.PhotoRepository;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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

    private final PhotoRepository photoRepository;

    public Long createPost(Long userId, PostCreateRequestDto requestDto) {
        User user = findUserById(userId);
        Schedule schedule = findScheduleById(requestDto.getScheduleId());
        Post post = Post.builder()
            .schedule(schedule)
            .user(user)
            .text(requestDto.getText())
            .title(requestDto.getTitle())
            .build();

        requestDto.getPhotoPaths()
            .forEach(photoPath ->{
                    Photo photo = new Photo(post, photoPath);
                }
            );

        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(initializePostInfo(postId));
    }

    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = initializePostInfo(postId);
        post.update(requestDto.getTitle(), requestDto.getText());
        List<Photo> addedPhotos = requestDto.getAddedPhotoPaths()
            .stream()
            .map(photoPath -> new Photo(post, photoPath))
            .collect(Collectors.toList());
        post.getPhotos()
            .removeAll(photoRepository.findPhotosByIdsInQuery(requestDto.getRemovedPhotoIds()));
        photoRepository.saveAll(addedPhotos);
//        photoRepository.deleteAllPhotosByIdInQuery(requestDto.getRemovedPhotoIds());
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RecordNotFoundException(
                    "해당 ID의 Post가 존재하지 않습니다."
                    , ErrorCode.POST_NOT_FOUND
            ));
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
            .orElseThrow(() -> new RecordNotFoundException(
                    "해당 ID의 User가 존재하지 않습니다."
                    , ErrorCode.USER_NOT_FOUND
            ));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RecordNotFoundException(
                    "해당 ID의 User가 존재하지 않습니다."
                    , ErrorCode.USER_NOT_FOUND
            ));
    }

}
