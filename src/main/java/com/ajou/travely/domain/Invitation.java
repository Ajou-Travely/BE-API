package com.ajou.travely.domain;

import com.ajou.travely.domain.travel.Travel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    private String email;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Column(columnDefinition = "BINARY(16)")
    private UUID code;

    public Invitation(String email, Travel travel, UUID code) {
        this.email = email;
        this.travel = travel;
        this.code = code;
    }
}
