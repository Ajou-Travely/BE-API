package com.ajou.travely.domain;

import javax.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private Long amount;
}
