package ru.shift.demo.simple_crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.constants.PeriodType;
import ru.shift.demo.simple_crm.dto.request.SellerStatisticsRequest;
import ru.shift.demo.simple_crm.exception.ResourceNotFoundException;
import ru.shift.demo.simple_crm.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void getMostProductiveSeller_ShouldReturnSeller_WhenFound() {
        // Given
        Seller topSeller = Seller.builder()
                .id(1L)
                .name("John Smith")
                .build();

        when(transactionRepository.findTopSellerInPeriod(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(topSeller));

        // When
        var result = statisticsService.getMostProductiveSeller(PeriodType.MONTH);

        // Then
        assertEquals("John Smith", result.name());
        verify(transactionRepository).findTopSellerInPeriod(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getMostProductiveSeller_ShouldThrowException_WhenNotFound() {
        // Given
        when(transactionRepository.findTopSellerInPeriod(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When & then
        assertThrows(ResourceNotFoundException.class,
                () -> statisticsService.getMostProductiveSeller(PeriodType.DAY));
    }

    @Test
    void getSellersUnderThreshold_ShouldReturnList() {
        // Given
        var request = new SellerStatisticsRequest(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now(),
                new BigDecimal("1000.00")
        );

        List<Seller> lowSalesSellers = List.of(
                Seller.builder().id(1L).name("Seller A").build(),
                Seller.builder().id(2L).name("Seller B").build()
        );

        when(transactionRepository.findSellersWithTotalTransactionsAmountLessThan(
                eq(request.start()), eq(request.end()), eq(request.maxTotalAmount())))
                .thenReturn(lowSalesSellers);

        // When
        var result = statisticsService.getSellersUnderThreshold(request);

        // Then
        assertEquals(2, result.size());
        assertEquals("Seller A", result.getFirst().name());
        verify(transactionRepository).findSellersWithTotalTransactionsAmountLessThan(any(), any(), any());
    }
}