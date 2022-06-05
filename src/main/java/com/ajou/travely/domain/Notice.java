package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany(mappedBy = "notice")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Notice(@NotBlank String title,
                  String content,
                  @NonNull User author,
                  List<Photo> photos,
                  LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.photos = photos;
        this.createdAt = createdAt;
    }
}
