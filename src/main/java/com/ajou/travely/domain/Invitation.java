package com.ajou.travely.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Invitation(String email, Travel travel) {
        this.email = email;
        this.travel = travel;
    }
}
