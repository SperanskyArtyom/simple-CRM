package ru.shift.demo.simple_crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.domain.entity.Transaction;
import ru.shift.demo.simple_crm.domain.entity.constants.PaymentType;
import ru.shift.demo.simple_crm.dto.request.CreateTransactionRequest;
import ru.shift.demo.simple_crm.exception.ResourceNotFoundException;
import ru.shift.demo.simple_crm.repository.SellerRepository;
import ru.shift.demo.simple_crm.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getTransactionsBySellerId_ShouldReturnList_WhenSellerExists() {
        // Given
        Long sellerId = 1L;
        Seller seller = Seller.builder()
                .id(sellerId)
                .build();
        when(sellerRepository.existsById(sellerId)).thenReturn(true);
        when(transactionRepository.findAllBySellerId(sellerId)).thenReturn(List.of(
                Transaction.builder()
                        .id(10L)
                        .seller(seller)
                        .amount(new BigDecimal("100.00"))
                        .paymentType(PaymentType.CASH)
                        .transactionDate(LocalDateTime.now())
                        .build()
        ));

        // When
        var result = transactionService.getTransactionsBySellerId(sellerId);

        // Then
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("100.00"), result.getFirst().amount());
        verify(transactionRepository).findAllBySellerId(sellerId);
    }

    @Test
    void getTransactionsBySellerId_ShouldThrowException_WhenSellerNotFound() {
        // Given
        when(sellerRepository.existsById(anyLong())).thenReturn(false);

        // When & then
        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionsBySellerId(999L));
    }

    @Test
    void create_ShouldReturnResponse_WhenSellerExists() {
        // Given
        Long sellerId = 1L;
        var request = new CreateTransactionRequest(sellerId, new BigDecimal("500.00"), PaymentType.CARD);
        var seller = Seller.builder().id(sellerId).build();
        var savedTransaction = Transaction.builder()
                .id(1L)
                .seller(seller)
                .amount(new BigDecimal("500.00"))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // When
        var result = transactionService.create(request);

        // Then
        assertNotNull(result.id());
        assertEquals(new BigDecimal("500.00"), result.amount());
        verify(sellerRepository).findById(sellerId);
        verify(transactionRepository).save(any(Transaction.class));
    }
}