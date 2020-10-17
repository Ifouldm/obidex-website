package com.obidex.webserver.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.obidex.webserver.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(Arrays.asList(ADMIN_READ, ADMIN_WRITE)),
    USER(Arrays.asList(USER_READ));

    @Getter
    private final List<ApplicationUserPermission> permissions;

    ApplicationUserRole(List<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthotities() {
        List<SimpleGrantedAuthority> userPpermissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        userPpermissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return userPpermissions;
    }
}
