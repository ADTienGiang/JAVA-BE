package com.vannguyen.java_learn_ecom.modules.order.domain;

public record ShippingAddress(
        String receiverName,
        String phone,
        String addressLine,
        String ward,
        String district,
        String city
) {
    public ShippingAddress {
        if (isBlank(receiverName)) {
            throw new IllegalArgumentException("Receiver name must not be blank");
        }

        if (isBlank(phone)) {
            throw new IllegalArgumentException("Phone must not be blank");
        }

        if (isBlank(addressLine)) {
            throw new IllegalArgumentException("Address line must not be blank");
        }

        if (isBlank(ward)) {
            throw new IllegalArgumentException("Ward must not be blank");
        }

        if (isBlank(district)) {
            throw new IllegalArgumentException("District must not be blank");
        }

        if (isBlank(city)) {
            throw new IllegalArgumentException("City must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}