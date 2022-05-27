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

//    private LocalDate startDate;
//
//    private LocalDate endDate;

//    @Convert(converter = ScheduleOrderConverter.class)
//    private List<Long> scheduleOrder = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @OneToMany(mappedBy = "travel")
    private final List<UserTravel> userTravels = new ArrayList<>();

//    @Convert(converter = OrderConverter.class)
//    private List<Long> dateOrder = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private List<TravelDate> travelDates = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title,
            @NonNull Long managerId,
//            @NonNull LocalDate startDate,
//            @NonNull LocalDate endDate,
            String memo,
            TravelType travelType) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
//        this.startDate = startDate;
//        this.endDate = endDate;
        this.travelType = travelType;
//        setScheduleOrder(scheduleOrder);
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
//        this.startDate = requestDto.getStartDate() != null ? requestDto.getStartDate() : this.startDate;
//        this.endDate = requestDto.getEndDate() != null ? requestDto.getEndDate() : this.endDate;
        this.memo = requestDto.getMemo() != null ? requestDto.getMemo() : this.memo;
    }

}