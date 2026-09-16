package com.vannguyen.java_learn_ecom.modules.order.application;

public record CreateShippingAddressCommand(
        String receiverName,
        String phone,
        String addressLine,
        String ward,
        String district,
        String city
) {
}