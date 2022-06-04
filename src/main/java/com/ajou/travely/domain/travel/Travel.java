package com.ajou.travely.domain.travel;

import com.ajou.travely.controller.travel.dto.TravelUpdateRequestDto;
import com.ajou.travely.domain.Material;
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

    private Integer budget;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @OneToMany(mappedBy = "travel")
    private final List<UserTravel> userTravels = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private final List<TravelDate> travelDates = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private final List<Material> materials = new ArrayList<>();

    @Builder
    public Travel(@NonNull String title,
                  @NonNull Long managerId,
                  String memo,
                  TravelType travelType,
                  Integer budget,
                  @NonNull LocalDate startDate,
                  @NonNull LocalDate endDate) {
        this.title = title;
        this.managerId = managerId;
        this.memo = memo;
        this.travelType = travelType;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addUserTravel(UserTravel userTravel) {
        userTravel.setTravel(this);
    }

    public void addMaterial(Material material) { this.materials.add(material); }

    public void removeMaterial(Material material) { this.materials.remove(material); }

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
        this.travelType = this.travelType == null ? TravelType.PRIVATE : this.travelType;
    }

    public void setTravelType(TravelType type) {
        this.travelType = type;
    }

    public void updateTravel(TravelUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : this.title;
        this.memo = requestDto.getMemo() != null ? requestDto.getMemo() : this.memo;
        this.budget = requestDto.getBudget() != null ? requestDto.getBudget() : this.budget;
    }

    public void updateDate(@NonNull LocalDate startDate, @NonNull LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}