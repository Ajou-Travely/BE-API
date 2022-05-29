package com.ajou.travely.domain.travel;

import com.ajou.travely.converter.OrderConverter;
import com.ajou.travely.domain.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
//@Table(
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "uniqueConstraint",
//                        columnNames = {"travel_id", "date"}
//                )
//        }
//)
@Entity
@NoArgsConstructor
public class TravelDate {
// TODO: unique key 두 개를 통해 생성 삭제하는 것으로 변경.
    private String title;

    @EmbeddedId
    private TravelDateIds travelDateIds;

    @MapsId("travelId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", referencedColumnName = "travel_id")
    private Travel travel;

    @Convert(converter = OrderConverter.class)
    private List<Long> scheduleOrder = new ArrayList<>();

    @OneToMany(mappedBy = "travelDate", orphanRemoval = true, cascade = CascadeType.ALL)
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
//            LocalDate date,
            @NonNull Travel travel,
            @NonNull TravelDateIds travelDateIds
    ) {
        this.title = title;
        this.travelDateIds = travelDateIds;
    }
}
