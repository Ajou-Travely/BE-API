package com.ajou.travely.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @OneToMany(mappedBy = "travel")
    private List<UserTravel> userTravels = new ArrayList<>();

    @Builder(builderMethodName = "noMemoBuilder")
    public Travel(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Builder
    public Travel(String title, String memo, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addUserTravel(UserTravel userTravel) {
        this.userTravels.add(userTravel);
    }
}
