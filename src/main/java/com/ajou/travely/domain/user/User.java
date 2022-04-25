package com.ajou.travely.domain.user;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false, length = 400)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(length = 5)
    private String sex;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    private Long kakaoId;

    private LocalDate birthday;

}
