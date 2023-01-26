package com.codevumc.codev_backend.domain;

import com.codevumc.codev_backend.domain.role.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoUser implements UserDetails {
    private String co_email;
    private String co_password;
    private String co_nickName;
    private String co_name;
    private String co_birth;
    private String co_gender;
    private Role role;
    private String profileImg;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return co_email;
    }

    @Override
    public String getPassword() { return co_password; }

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

    public String getLoginJson(String co_email, String co_password) {
        return "{\n" +
                "    \"co_email\":\"" + co_email + "\",\n" +
                "    \"co_password\":\"" + co_password + "\"\n" +
                "}";
    }
}
