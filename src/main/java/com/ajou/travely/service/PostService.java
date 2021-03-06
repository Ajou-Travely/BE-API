package com.ajou.travely.service;

import com.ajou.travely.controller.post.dto.PostCreateRequestDto;
import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.PostUpdateRequestDto;
import com.ajou.travely.domain.Friend;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.FriendRepository;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    private final FriendRepository friendRepository;

    private final PhotoService photoService;

    @PersistenceContext
    private final EntityManager em;

    public PostResponseDto createPost(Long userId, PostCreateRequestDto requestDto) {
        User user = findUserById(userId);
        Schedule schedule = findScheduleById(requestDto.getScheduleId());
        Post post = Post.builder()
            .schedule(schedule)
            .user(user)
            .text(requestDto.getText())
            .title(requestDto.getTitle())
            .build();

        if (requestDto.getPhotos() != null) {
            if (!requestDto.getPhotos().isEmpty()) {
                photoService.createPhotos(post, requestDto.getPhotos());
            }
        }

        return new PostResponseDto(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(initializePostInfo(postId));
    }

    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostResponseDto::new);
    }

    public Page<PostResponseDto> getPostsOfFriends(Long userId, Pageable pageable) {
        List<Long> friendIds = friendRepository
                .findAllFriendsByFollowee(userId, pageable)
                .map(friend -> friend.getFollower().getId())
                .toList();
        return postRepository.findAllPostsByUserIds(friendIds, pageable).map(PostResponseDto::new);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = findPostById(postId);
        post.update(requestDto.getTitle(), requestDto.getText());

        if (requestDto.getAddPhotos() != null) {
            photoService.createPhotos(post, requestDto.getAddPhotos());
        }
        if (requestDto.getRemovePhotoIds() != null) {
            photoService.removePhotoIds(requestDto.getRemovePhotoIds());
        }

        em.flush();
        em.clear();

        Post returnPost = findPostById(postId);
        return new PostResponseDto(returnPost);
    }

    public void deletePost(Long postId){
        photoService.removePhotos(findPostById(postId).getPhotos());
        postRepository.deleteById(postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RecordNotFoundException(
                    "?????? ID??? Post??? ???????????? ????????????. id=" + postId
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
                    "?????? ID??? User??? ???????????? ????????????. id=" + userId
                    , ErrorCode.USER_NOT_FOUND
            ));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RecordNotFoundException(
                    "?????? ID??? Schedule??? ???????????? ????????????. id=" + scheduleId
                    , ErrorCode.SCHEDULE_NOT_FOUND
            ));
    }

}
