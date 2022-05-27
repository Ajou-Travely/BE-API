package com.ajou.travely.domain;

import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_date_id")
    private TravelDate travelDate;

    @OneToMany(mappedBy = "schedule")
    private List<Branch> branches;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    public Schedule(@NonNull Travel travel, @NonNull Place place, LocalDateTime startTime, LocalDateTime endTime) {
        this.travel = travel;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.branches = new ArrayList<>();
    }

    public void addUser(Branch branch) {
        branch.setSchedule(this);
    }

    public void removeUser(Branch branch) {
        this.branches.remove(branch);
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
