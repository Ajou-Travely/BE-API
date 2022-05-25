package com.ajou.travely.domain;

import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.DuplicatedRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", referencedColumnName = "user_id")
    private User followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follwer_id", referencedColumnName = "user_id")
    private User follower;

    private Boolean isFriend;

    public Friend(User followee, User follower) {
        this.follower = follower;
        this.followee = followee;
        this.isFriend = false;
    }

    public Friend(User followee, User follower, Boolean isFriend) {
        this.follower = follower;
        this.followee = followee;
        this.isFriend = isFriend;
    }

    public void acceptFriendRequest() {
        if(this.isFriend) {
            throw new DuplicatedRequestException(
                    "해당 user와 이미 친구 상태입니다.",
                    ErrorCode.ALREADY_FRIEND
            );
        }
        this.isFriend = true;
    }
}
