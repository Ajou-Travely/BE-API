package com.ajou.travely.domain.travel;

import com.ajou.travely.domain.UserTravel;
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

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @OneToMany(mappedBy = "travel")
    private final List<UserTravel> userTravels = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title, @NonNull Long managerId, String memo,
        @NonNull LocalDate startDate, @NonNull LocalDate endDate,
        List<Long> scheduleOrder, TravelType travelType) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelType = travelType;
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

    @PrePersist
    public void prePersist() {
        this.travelType = this.travelType == null ? TravelType.PUBLIC : this.travelType;
    }

    public void setTravelType(TravelType type) {
        this.travelType = type;
    }
}