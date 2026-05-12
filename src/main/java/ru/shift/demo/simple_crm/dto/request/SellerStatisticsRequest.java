package ru.shift.demo.simple_crm.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SellerStatisticsRequest(
        @NotNull(message = "Start date is required")
        LocalDateTime start,

        @NotNull(message = "End date is required")
        LocalDateTime end,

        @NotNull(message = "Max total amount is required")
        @Positive(message = "Max total amount must be greater than zero")
        BigDecimal maxTotalAmount
) {
}
