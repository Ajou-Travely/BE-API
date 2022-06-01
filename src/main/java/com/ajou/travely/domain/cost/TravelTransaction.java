package com.ajou.travely.domain.cost;

import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class TravelTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Column(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Column(name = "receiver_id")
    private User receiver;

    private Long amount;
}
