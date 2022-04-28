package com.ajou.travely.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="travel_id")
    private Travel travel;

    private Long totalAmount;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String title;

    @Builder
    public Cost(@NonNull Travel travel, @NonNull Long totalAmount, String content, @NonNull String title) {
        this.travel = travel;
        this.totalAmount = totalAmount;
        this.content = content;
        this.title = title;
    }
}
