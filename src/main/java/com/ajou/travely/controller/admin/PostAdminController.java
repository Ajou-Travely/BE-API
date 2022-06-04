package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.post.dto.PostResponseDto;
import com.ajou.travely.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/posts")
@RestController
public class PostAdminController {

    private final PostService postService;

    @GetMapping()
    public ResponseEntity<Page<PostResponseDto>> showAllPosts(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }
}
