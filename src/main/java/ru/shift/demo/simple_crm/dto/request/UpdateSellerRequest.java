package ru.shift.demo.simple_crm.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateSellerRequest(
        @Size(max = 100)
        String name,

        @Size(max = 255)
        String contactInfo
) {
}
