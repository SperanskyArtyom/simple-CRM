package ru.shift.demo.simple_crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.request.CreateSellerRequest;
import ru.shift.demo.simple_crm.dto.request.UpdateSellerRequest;
import ru.shift.demo.simple_crm.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    void create_ShouldReturnResponse_WhenRequestIsValid() {
        // Given
        var request = new CreateSellerRequest("John Smith", "john@example.com");
        var savedEntity = Seller.builder()
                .id(1L)
                .name("John Smith")
                .contactInfo("john@example.com")
                .registrationDate(LocalDateTime.now())
                .build();

        when(sellerRepository.save(any(Seller.class))).thenReturn(savedEntity);

        // When
        var result = sellerService.create(request);

        // Then
        assertNotNull(result.id());
        assertEquals("John Smith", result.name());
        verify(sellerRepository).save(any(Seller.class));
    }

    @Test
    void getById_ShouldReturnSeller_WhenExists() {
        // Given
        Long id = 1L;
        var seller = Seller.builder().id(id).name("John Smith").build();
        when(sellerRepository.findById(id)).thenReturn(Optional.of(seller));

        // When
        var result = sellerService.getById(id);

        // Then
        assertEquals(id, result.id());
        assertEquals("John Smith", result.name());
    }

    @Test
    void getAll_ShouldReturnList() {
        // Given
        when(sellerRepository.findAll()).thenReturn(List.of(
                Seller.builder().id(1L).name("First").build(),
                Seller.builder().id(2L).name("Second").build()
        ));

        // When
        var result = sellerService.getAll();

        // Then
        assertEquals(2, result.size());
        verify(sellerRepository).findAll();
    }
}