package com.vannguyen.java_learn_ecom.modules.order.presentation;

public record ShippingAddressResponse(
        String receiverName,
        String phone,
        String addressLine,
        String ward,
        String district,
        String city
) {
}