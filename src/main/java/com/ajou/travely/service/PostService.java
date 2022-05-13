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

        if (requestDto.getPhotos() != null) {
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
        photoService.updatePhotos(post, requestDto.getAddPhotos(), requestDto.getRemovePhotoIds());
//        List<Photo> addedPhotos = requestDto.getAddedPhotoPaths()
//            .stream()
//            .map(photoName -> new Photo(post, photoName))
//            .collect(Collectors.toList());
//        post.getPhotos()
//            .removeAll(photoRepository.findPhotosByIdsInQuery(requestDto.getRemovedPhotoIds()));
//        photoRepository.saveAll(addedPhotos);
//        photoRepository.deleteAllPhotosByIdInQuery(requestDto.getRemovedPhotoIds());
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글 없음 ㅋㅋ"));
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
            .orElseThrow(() -> new RuntimeException("유저 없음 ㅋㅋ"));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("스케줄 없음 ㅋㅋ"));
    }

}
