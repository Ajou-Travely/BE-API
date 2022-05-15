package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class UserTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_travel_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Builder
    public UserTravel(@NonNull User user, @NonNull Travel travel) {
        this.user = user;
        setTravel(travel);
    }

    public void setTravel(Travel travel) {
        if (Objects.nonNull(this.travel)) {
            this.travel.getUserTravels().remove(this);
        }
        this.travel = travel;
        travel.getUserTravels().add(this);
    }
}
