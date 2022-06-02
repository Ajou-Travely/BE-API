package com.ajou.travely.domain.cost;

import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class TravelTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Long amount;

    @Builder
    public TravelTransaction(Travel travel,
                             User sender,
                             User receiver,
                             User createdBy,
                             Long amount) {
        this.travel = travel;
        this.sender = sender;
        this.receiver = receiver;
        this.createdBy = createdBy;
        this.amount = amount;
    }

    public void updateTravelTransaction(User sender,
                                   User receiver,
                                   User createdBy,
                                   Long amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.createdBy = createdBy;
        this.amount = amount;
    }
}
