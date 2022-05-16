package com.ajou.travely.config.auth;

import com.ajou.travely.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class CustomAuthentication implements Authentication {
    private final User user;

    public CustomAuthentication(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityCollection = new ArrayList<>();
        grantedAuthorityCollection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getType().name();
            }
        });
        return grantedAuthorityCollection;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user.getKakaoId();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
