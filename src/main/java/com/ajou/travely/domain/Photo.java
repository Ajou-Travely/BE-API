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

    private String path;

    @Builder
    public Photo(@NonNull Post post, @NonNull String path) {
        this.post = post;
        this.path = path;
    }
}
