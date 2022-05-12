package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Branch(@NonNull User user, @NonNull Schedule schedule) {
        this.user = user;
        setSchedule(schedule);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSchedule(Schedule schedule) {
        if (Objects.nonNull(this.schedule)) {
            this.schedule.getBranches().remove(this);
        }
        this.schedule = schedule;
        schedule.getBranches().add(this);
    }
}
