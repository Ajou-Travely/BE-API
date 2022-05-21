package com.ajou.travely.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private String scheduleOrder;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @OneToMany(mappedBy = "travel")
    private final List<UserTravel> userTravels = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title, @NonNull Long managerId, String memo, @NonNull LocalDate startDate, @NonNull LocalDate endDate, List<Long> scheduleOrder) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
        setScheduleOrder(scheduleOrder);
    }

    public void addUserTravel(UserTravel userTravel) {
        userTravel.setTravel(this);
    }

    public void setScheduleOrder(List<Long> scheduleOrder) {
        if (Objects.isNull(scheduleOrder)) {
            scheduleOrder = new ArrayList<>();
        }
        this.scheduleOrder = scheduleOrder
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
