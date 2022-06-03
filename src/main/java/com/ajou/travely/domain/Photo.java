package com.ajou.travely.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String name;

    @Builder
    public Photo(@NonNull Post post, @NonNull String name) {
        this.post = post;
        this.name = name;
        post.getPhotos().add(this);
    }

    @Builder
    public Photo(@NonNull Notice notice, @NonNull String name) {
        this.notice = notice;
        this.name = name;
        notice.getPhotos().add(this);
    }
}
