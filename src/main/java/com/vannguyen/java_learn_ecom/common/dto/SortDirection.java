package com.vannguyen.java_learn_ecom.common.dto;

public enum SortDirection {
    ASC,
    DESC;

    public boolean isAscending() {
        return this == ASC;
    }
}