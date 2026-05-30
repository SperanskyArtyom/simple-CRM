package ru.shift.demo.simple_crm.dto.response;

import java.time.Instant;

public record SellerResponse(
        Long id,
        String name,
        String contactInfo,
        Instant registrationDate
) {
}
