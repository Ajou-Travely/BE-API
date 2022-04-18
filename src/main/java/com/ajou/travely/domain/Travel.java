package com.ajou.travely.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Travel {
    @Id
    @GeneratedValue
    @Column(name = "travel_id")
    private Long id;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String memo;
}
