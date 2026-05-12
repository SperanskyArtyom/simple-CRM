package ru.shift.demo.simple_crm.dto.response;

import java.time.LocalDateTime;

public record SellerResponse(
        Long id,
        String name,
        String contactInfo,
        LocalDateTime registrationDate
) {
}
