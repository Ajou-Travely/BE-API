package com.ajou.travely.domain.travel;

import com.ajou.travely.converter.OrderConverter;
import com.ajou.travely.domain.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniqueConstraint",
                        columnNames = {"travel_id", "date"}
                )
        }
)
@Entity
public class TravelDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_date_id")
    private Long id;

    private String title;

    private LocalDate date;

    @Convert(converter = OrderConverter.class)
    private List<Long> scheduleOrder = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @OneToMany(mappedBy = "travelDate")
    private List<Schedule> schedules = new ArrayList<>();

    public void setScheduleOrder(List<Long> scheduleOrder) {
        if(Objects.isNull(scheduleOrder)) {
            this.scheduleOrder = new ArrayList<>();
        } else {
            this.scheduleOrder = scheduleOrder;
        }
    }

    @Builder
    public TravelDate(
            String title,
            LocalDate date,
            @NonNull Travel travel
    ) {
        this.title = title;
        this.date = date;
        this.travel = travel;
    }
}
