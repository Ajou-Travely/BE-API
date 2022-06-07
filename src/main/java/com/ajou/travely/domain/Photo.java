package com.ajou.travely.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Photo<T> {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

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

    @Builder
    public Photo(@NonNull Event event, @NonNull String name) {
        this.event = event;
        this.name = name;
        event.getPhotos().add(this);
    }

    @Builder
    public Photo(@NonNull T record, @NonNull String name) {
        if (record.getClass().equals(Event.class)) {
            this.event = (Event) record;
            this.name = name;
            event.getPhotos().add(this);
        } else if(record.getClass().equals(Notice.class)) {
            this.notice = (Notice) record;
            this.name = name;
            notice.getPhotos().add(this);
        } else if(record.getClass().equals(Post.class)) {
            this.post = (Post) record;
            this.name = name;
            post.getPhotos().add(this);
        }
    }
}
