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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "travel_id")
//    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "date"),
            @JoinColumn(name = "travel_id")
    })
    private TravelDate travelDate;

    @OneToMany(mappedBy = "schedule")
    private List<Branch> branches;

    @OneToMany(mappedBy = "schedule")
    private final List<SchedulePhoto> photos = new ArrayList<>();

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    public Schedule(@NonNull TravelDate travelDate, @NonNull Place place, LocalDateTime startTime, LocalDateTime endTime) {
//        this.travel = travel;
        this.travelDate = travelDate;
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

    public void addSchedulePhotos(List<SchedulePhoto> photo) {
        this.photos.addAll(photo);
    }

    public void removeSchedulePhotos(List<SchedulePhoto> photo) {
        this.photos.removeAll(photo);
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
