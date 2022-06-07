package com.ajou.travely.domain;

import com.ajou.travely.controller.event.dto.EventUpdateDto;
import com.ajou.travely.controller.notice.dto.NoticeUpdateDto;
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
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany(mappedBy = "event")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Event(@NotBlank String title,
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

    public void update(EventUpdateDto eventUpdateDto) {
        if (eventUpdateDto.getTitle() != null) {
            this.title = eventUpdateDto.getTitle();
        }
        if (eventUpdateDto.getContent() != null) {
            this.content = eventUpdateDto.getContent();
        }
    }
}
