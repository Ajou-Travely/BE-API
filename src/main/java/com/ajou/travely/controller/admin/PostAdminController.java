package com.ajou.travely.controller.admin;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.controller.post.dto.PostUpdateRequestDto;
import com.ajou.travely.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/posts")
@RestController
public class PostAdminController {

    private final PostService postService;

    @GetMapping()
    public ResponseEntity<Page<PostResponseDto>> showAllPosts(@PageableDefault(
            sort = {"id"},
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> showPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<PostResponseDto>> showPostsOfFriends(@PathVariable Long userId,
                                                                    @PageableDefault(
                                                                            sort = {"id"},
                                                                            direction = Sort.Direction.DESC
                                                                    ) Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsOfFriends(userId, pageable));
    }
}
