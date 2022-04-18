package com.ajou.travely.domain;

import javax.persistence.*;

@Entity
public class Cost {
    @Id
    @GeneratedValue
    @Column(name="cost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="travel_id")
    private Travel travel;

    private Long totalAmount;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String title;
}
