package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
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
public class Post {
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
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Post(@NonNull Schedule schedule, @NonNull User user, String text, @NonNull String title) {
        this.schedule = schedule;
        this.user = user;
        this.text = text;
        this.title = title;
    }

    public void update(@NotBlank String title,@NonNull String text) {
        this.title = title;
        this.text = text;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    @Override
    public String toString() {
        return "id: " + this.id.toString() +
                "title: " + this.title +
                "text: " + this.text;
    }
}
