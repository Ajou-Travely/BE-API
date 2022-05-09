package com.ajou.travely.controller.post;

import com.ajou.travely.controller.post.dto.*;
import com.ajou.travely.domain.Post;
import com.ajou.travely.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Long> createPost(Long userId,
        @Valid @RequestBody PostCreateRequestDto requestDto) {
        Long postId = postService.createPost(userId, requestDto);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        Post post = postService.findPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
        @Valid @RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(postId, requestDto);
        return ResponseEntity.ok()
            .build();
    }

}
