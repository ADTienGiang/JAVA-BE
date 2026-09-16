package com.vannguyen.java_learn_ecom.modules.order.presentation;

import jakarta.validation.constraints.NotBlank;

public record ShippingAddressRequest(
        @NotBlank(message = "Receiver name must not be blank")
        String receiverName,

        @NotBlank(message = "Phone must not be blank")
        String phone,

        @NotBlank(message = "Address line must not be blank")
        String addressLine,

        @NotBlank(message = "Ward must not be blank")
        String ward,

        @NotBlank(message = "District must not be blank")
        String district,

        @NotBlank(message = "City must not be blank")
        String city
) {
}