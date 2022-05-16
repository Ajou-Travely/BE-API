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
    @NonNull
    private Type type;

    @Column(length = 400, unique = true)
    @NonNull
    private String email;

    @Column(length = 20)
    @NonNull
    private String name;

    @Column(length = 30)
    @NonNull
    private String phoneNumber;

    @Column(length = 5)
    private String sex;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    private Long kakaoId;

    private LocalDate birthday;

//    private List<Post> posts;

    @Builder
    public User(@NonNull Type type, @NonNull String email, @NonNull String name, @NonNull String phoneNumber, @NonNull Long kakaoId) {
        this.type = type;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
    }
}
