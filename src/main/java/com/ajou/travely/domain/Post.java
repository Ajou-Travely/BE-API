package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String text;

    private String title;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(@NonNull Schedule schedule, @NonNull User user, String text, @NonNull String title, LocalDateTime createdAt) {
        this.schedule = schedule;
        this.user = user;
        this.text = text;
        this.title = title;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now().withNano(0);
    }

    public void update(@NotBlank String title,@NonNull String text) {
        this.title = title;
        this.text = text;
    }

}
