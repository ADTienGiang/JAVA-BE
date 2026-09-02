package com.vannguyen.java_learn_ecom.common.dto;

import java.util.List;

public record PageResult<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static <T> PageResult<T> of(
            List<T> items,
            PageQuery pageQuery,
            long totalItems
    ) {
        int totalPages = calculateTotalPages(totalItems, pageQuery.size());

        return new PageResult<>(
                items,
                pageQuery.page(),
                pageQuery.size(),
                totalItems,
                totalPages,
                pageQuery.page() < totalPages,
                pageQuery.page() > 1
        );
    }

    private static int calculateTotalPages(long totalItems, int size) {
        if (totalItems == 0) {
            return 0;
        }

        return (int) Math.ceil((double) totalItems / size);
    }
}