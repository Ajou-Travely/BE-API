package com.ajou.travely.service;

import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.UserRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    public Post createPost(Long userId, Long scheduleId, String title, String text){
        User user = findUserById(userId);
        Schedule schedule = findScheduleById(scheduleId);
        Post post = Post.builder()
            .schedule(schedule)
            .user(user)
            .text(text)
            .title(title)
            .build();

        return postRepository.save(post);
    }

    public Post findPostById(Long postId){
        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글 없음 ㅋㅋ"));
    }

    public void updatePost(Long postId, String title, String text){
        Post post = findPostById(postId);
        post.update(title, text);
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음 ㅋㅋ"));
    }

    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("스케줄 없음 ㅋㅋ"));
    }

}
