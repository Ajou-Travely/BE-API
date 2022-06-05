package com.ajou.travely.service;

import com.ajou.travely.controller.comment.dto.CommentCreateRequestDto;
import com.ajou.travely.controller.comment.dto.CommentResponseDto;
import com.ajou.travely.controller.comment.dto.CommentUpdateDto;
import com.ajou.travely.domain.Comment;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.CommentRepository;
import com.ajou.travely.repository.PostRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long userId, Long postId, CommentCreateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 게시물을 찾을 수 없습니다.",
                        ErrorCode.POST_NOT_FOUND
                ));
        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .post(post)
                .content(requestDto.getContent())
                .build());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long userId,
                                            Long commentId,
                                            CommentUpdateDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 댓글을 찾을 수 없습니다.",
                        ErrorCode.COMMENT_NOT_FOUND
                ));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("댓글 작성자가 아니기 때문에 수정할 수 없습니다.");
        }

        comment.update(requestDto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long userId ,Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 댓글을 찾을 수 없습니다.",
                        ErrorCode.COMMENT_NOT_FOUND
                ));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("댓글 작성자가 아니기 때문에 수정할 수 없습니다.");
        }
        commentRepository.delete(comment);
    }
}
