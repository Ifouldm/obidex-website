package com.obidex.webserver.security;

import lombok.Getter;

public enum ApplicationUserPermission {
    USER_READ("user:read"),
    ADMIN_READ("user:read"),
    ADMIN_WRITE("user:write");

    @Getter
    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
