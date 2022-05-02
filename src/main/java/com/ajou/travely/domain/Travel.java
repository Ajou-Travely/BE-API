package com.ajou.travely.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;

    @NotNull
    private Long managerId;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @OneToMany(mappedBy = "travel")
    private List<UserTravel> userTravels = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title, @NonNull Long managerId, String memo, @NonNull LocalDate startDate, @NonNull LocalDate endDate) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addUserTravel(UserTravel userTravel) {
        this.userTravels.add(userTravel);
    }
}
