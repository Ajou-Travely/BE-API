package com.ajou.travely.domain.travel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Travel travel;
}
