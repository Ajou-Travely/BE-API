package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private Boolean isEquallyDivided;

    @OneToMany(mappedBy = "cost")
    private List<UserCost> userCosts = new ArrayList<>();

    private Long payerId;

    @Builder
    public Cost(@NonNull Travel travel, @NonNull Long totalAmount, String content, @NonNull String title, @NonNull Boolean isEquallyDivided, @NonNull Long payerId) {
        this.travel = travel;
        this.payerId = payerId;
        this.totalAmount = totalAmount;
        this.content = content;
        this.title = title;
        this.isEquallyDivided = isEquallyDivided;
    }

    public void addUserCost(UserCost userCost) {
        this.userCosts.add(userCost);
    }
}
