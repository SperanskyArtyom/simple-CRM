package ru.shift.demo.simple_crm.dto.response;

import ru.shift.demo.simple_crm.domain.entity.constants.PaymentType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        Long sellerId,
        BigDecimal amount,
        PaymentType paymentType,
        Instant transactionDate
) {
}
