package com.ajou.travely.domain.travel;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TravelDateIds implements Serializable {
    private LocalDate date;
    private Long travelId;

    @Builder
    public TravelDateIds(LocalDate date, Long travelId) {
        this.date = date;
        this.travelId = travelId;
    }
}
