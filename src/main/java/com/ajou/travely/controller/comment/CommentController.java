package com.ajou.travely.controller.comment;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.comment.dto.CommentCreateRequestDto;
import com.ajou.travely.controller.comment.dto.CommentResponseDto;
import com.ajou.travely.controller.comment.dto.CommentUpdateDto;
import com.ajou.travely.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("v1/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(@LoginUser SessionUser sessionUser,
                                                            @PathVariable Long postId,
                                                            @Valid @RequestBody CommentCreateRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(sessionUser.getUserId(), postId, requestDto));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@LoginUser SessionUser sessionUser,
                                                            @PathVariable Long commentId,
                                                                    @Valid @RequestBody CommentUpdateDto requestDto) {

        return ResponseEntity.ok(commentService.updateComment(sessionUser.getUserId(), commentId, requestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@LoginUser SessionUser sessionUser,
                                              @PathVariable Long commentId) {
        commentService.deleteComment(sessionUser.getUserId(), commentId);
        return ResponseEntity.ok().build();
    }
}
