package ru.shift.demo.simple_crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.constants.PeriodType;
import ru.shift.demo.simple_crm.dto.request.SellerStatisticsRequest;
import ru.shift.demo.simple_crm.dto.response.SellerResponse;
import ru.shift.demo.simple_crm.exception.ResourceNotFoundException;
import ru.shift.demo.simple_crm.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    private final TransactionRepository transactionRepository;

    public SellerResponse getMostProductiveSeller(PeriodType periodType) {
        log.info("Calculating most productive seller for period: {}", periodType);

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = switch (periodType) {
            case DAY -> end.minusDays(1);
            case MONTH -> end.minusMonths(1);
            case QUARTER -> end.minusMonths(3);
            case YEAR -> end.minusYears(1);
        };

        return transactionRepository.findTopSellerInPeriod(start, end)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.warn("No top seller found for period: {}", periodType);
                    return new ResourceNotFoundException("No top seller found for this period");
                });
    }

    public List<SellerResponse> getSellersUnderThreshold(SellerStatisticsRequest request) {
        log.info("Fetching sellers with total sales less than {} between {} and {}",
                request.maxTotalAmount(), request.start(), request.end());

        return transactionRepository.findSellersWithTotalTransactionsAmountLessThan(
                        request.start(),
                        request.end(),
                        request.maxTotalAmount()
                ).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private SellerResponse mapToResponse(Seller entity) {
        return new SellerResponse(
                entity.getId(),
                entity.getName(),
                entity.getContactInfo(),
                entity.getRegistrationDate()
        );
    }
}
