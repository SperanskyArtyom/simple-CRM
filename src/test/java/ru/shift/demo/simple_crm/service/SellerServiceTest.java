package ru.shift.demo.simple_crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.request.UpdateSellerRequest;
import ru.shift.demo.simple_crm.repository.SellerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {
    @Mock
    private SellerRepository sellerRepository;
    @InjectMocks
    private SellerService sellerService;

    @Test
    void update_ShouldUpdateFields_WhenSellerExists() {
        // Given
        Long id = 1L;
        var existing = Seller.builder().id(id).name("Old Name").contactInfo("old@mail.ru").build();
        var request = new UpdateSellerRequest("New Name", "new@mail.ru");

        when(sellerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(sellerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var result = sellerService.update(id, request);

        // Then
        assertEquals("New Name", result.name());
        assertEquals("new@mail.ru", result.contactInfo());
    }

    @Test
    void delete_ShouldCallRepository_WhenSellerExists() {
        // Given
        when(sellerRepository.existsById(1L)).thenReturn(true);

        // When
        sellerService.delete(1L);

        // Then
        verify(sellerRepository).deleteById(1L);
    }
}