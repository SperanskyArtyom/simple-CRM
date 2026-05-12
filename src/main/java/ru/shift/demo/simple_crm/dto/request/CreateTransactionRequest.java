package ru.shift.demo.simple_crm.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.shift.demo.simple_crm.domain.entity.constants.PaymentType;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @NotNull(message = "Seller ID is required")
        Long sellerId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Payment type is required")
        PaymentType paymentType
) {
}
