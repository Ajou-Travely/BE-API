package com.ajou.travely.domain.user;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()
    @NotNull
    private UserType userType;

    @Column(length = 400, unique = true)
    @NotNull
    private String email;

    @Column(length = 20)
    @NotNull
    private String name;

    @Column(length = 30)
    @NotNull
    private String phoneNumber;

    @Column
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @NotNull
    private Long kakaoId;

    private LocalDate birthday;

//    private List<Post> posts;

    @Builder
    public User(@NonNull UserType userType,
                @NonNull String email,
                @NonNull String name,
                @NonNull String phoneNumber,
                @NonNull Long kakaoId,
                Sex sex,
                LocalDate birthday
    ) {
        this.userType = userType;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
        this.sex = sex;
        this.birthday = birthday;
    }
}
