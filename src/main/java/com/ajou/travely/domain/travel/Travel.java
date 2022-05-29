package com.ajou.travely.domain.travel;

import com.ajou.travely.controller.travel.dto.TravelUpdateRequestDto;
import com.ajou.travely.converter.OrderConverter;
import com.ajou.travely.domain.UserTravel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @OneToMany(mappedBy = "travel")
    private final List<UserTravel> userTravels = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private List<TravelDate> travelDates = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title,
            @NonNull Long managerId,
            String memo,
            TravelType travelType) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
        this.travelType = travelType;
    }

    public void addUserTravel(UserTravel userTravel) {
        userTravel.setTravel(this);
    }

//    public void setScheduleOrder(List<Long> scheduleOrder) {
//        if (Objects.isNull(scheduleOrder)) {
//            this.scheduleOrder = new ArrayList<>();
//        } else {
//            this.scheduleOrder = scheduleOrder;
//        }
//    }
//    public void setdateOrder(List<Long> dateOrder) {
//        if (Objects.isNull(dateOrder)) {
//            this.dateOrder = new ArrayList<>();
//        } else {
//            this.dateOrder = dateOrder;
//        }
//    }

    @PrePersist
    public void prePersist() {
        this.travelType = this.travelType == null ? TravelType.PUBLIC : this.travelType;
    }

    public void setTravelType(TravelType type) {
        this.travelType = type;
    }

    public void updateTravel(TravelUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : this.title;
        this.memo = requestDto.getMemo() != null ? requestDto.getMemo() : this.memo;
    }

}