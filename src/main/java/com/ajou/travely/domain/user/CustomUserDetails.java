package com.ajou.travely.domain.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private User user;

    private String accessToken;

    public CustomUserDetails(User user) {
        this(user, null);
    }

    public CustomUserDetails(User user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityCollection = new ArrayList<>();
        grantedAuthorityCollection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getUserType().getKey();
            }
        });
        return grantedAuthorityCollection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public Long getKakaoId() {
        return user.getKakaoId();
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
