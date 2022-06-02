package com.ajou.travely.controller.post;

import com.ajou.travely.controller.post.dto.*;
import com.ajou.travely.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<PostResponseDto> createPost(Long userId,
        @Valid @ModelAttribute PostCreateRequestDto requestDto) {
        return ResponseEntity.ok(postService.createPost(userId, requestDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponse = postService.getPost(postId);
        return ResponseEntity.ok(postResponse);
    }

    @PatchMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
        @Valid @ModelAttribute PostUpdateRequestDto requestDto) {
        return ResponseEntity.ok(postService.updatePost(postId, requestDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok()
            .build();
    }

}
