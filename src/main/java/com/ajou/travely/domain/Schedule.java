package com.ajou.travely.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    public Schedule(@NonNull Travel travel, @NonNull Place place, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime) {
        this.travel = travel;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
