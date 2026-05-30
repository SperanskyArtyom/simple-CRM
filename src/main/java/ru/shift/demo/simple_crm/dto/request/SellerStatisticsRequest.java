package ru.shift.demo.simple_crm.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record SellerStatisticsRequest(
        @NotNull(message = "Start date is required")
        Instant start,

        @NotNull(message = "End date is required")
        Instant end,

        @NotNull(message = "Max total amount is required")
        @Positive(message = "Max total amount must be greater than zero")
        BigDecimal maxTotalAmount
) {
}
