package com.vannguyen.java_learn_ecom.common.dto;

public record PageQuery(
        int page,
        int size
) {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    public PageQuery {
        if (page < 1) {
            page = DEFAULT_PAGE;
        }

        if (size < 1) {
            size = DEFAULT_SIZE;
        }

        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
    }

    public int offset() {
        return (page - 1) * size;
    }
}