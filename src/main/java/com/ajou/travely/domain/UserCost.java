package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class UserCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_id")
    private Cost cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long amount;
    
    @ColumnDefault("false")
    private Boolean isRequested;

    @Builder
    public UserCost(@NonNull Cost cost, @NonNull User user, @NonNull Long amount) {
        this.cost = cost;
        this.user = user;
        this.amount = amount;
        this.isRequested = false;
    }
}
