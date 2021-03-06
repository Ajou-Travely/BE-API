package com.ajou.travely.domain.cost;


import com.ajou.travely.controller.cost.dto.CostUpdateDto;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.travel.Travel;
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

    @OneToMany(mappedBy = "cost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCost> userCosts = new ArrayList<>();

    private Long payerId;

    @Builder
    public Cost(@NonNull Travel travel, @NonNull Long totalAmount, String content, @NonNull String title, @NonNull Long payerId) {
        this.travel = travel;
        this.payerId = payerId;
        this.totalAmount = totalAmount;
        this.content = content;
        this.title = title;
    }

    public void addUserCost(UserCost userCost) {
        this.userCosts.add(userCost);
    }

    public void removeUserCost(UserCost userCost) {
        this.userCosts.remove(userCost);
    }

    public void updateCost(CostUpdateDto costUpdateDto) {
        this.totalAmount = costUpdateDto.getTotalAmount();
        this.content = costUpdateDto.getContent();
        this.title = costUpdateDto.getTitle();
        this.payerId = costUpdateDto.getPayerId();
    }

}
