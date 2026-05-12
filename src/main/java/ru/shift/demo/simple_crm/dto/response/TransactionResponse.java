package ru.shift.demo.simple_crm.dto.response;

import ru.shift.demo.simple_crm.domain.entity.constants.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long sellerId,
        BigDecimal amount,
        PaymentType paymentType,
        LocalDateTime transactionDate
) {
}
