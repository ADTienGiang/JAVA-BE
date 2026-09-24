package com.vannguyen.java_learn_ecom.modules.auth.domain;

import java.util.Set;

public enum UserRole {
    CUSTOMER(Set.of(
            UserPermission.ORDER_READ,
            UserPermission.ORDER_CANCEL
    )),

    ADMIN(Set.of(
            UserPermission.PRODUCT_CREATE,
            UserPermission.PRODUCT_UPDATE,
            UserPermission.PRODUCT_DELETE,
            UserPermission.ORDER_READ,
            UserPermission.ORDER_CANCEL
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }
}