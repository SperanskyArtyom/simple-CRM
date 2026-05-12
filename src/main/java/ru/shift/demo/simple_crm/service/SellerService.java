package ru.shift.demo.simple_crm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.request.CreateSellerRequest;
import ru.shift.demo.simple_crm.dto.request.UpdateSellerRequest;
import ru.shift.demo.simple_crm.dto.response.SellerResponse;
import ru.shift.demo.simple_crm.exception.ResourceNotFoundException;
import ru.shift.demo.simple_crm.repository.SellerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {
    private final SellerRepository sellerRepository;

    @Transactional
    public SellerResponse create(CreateSellerRequest request) {
        log.info("Creating new seller with name: {}", request.name());
        Seller seller = mapToEntity(request);
        Seller saved = sellerRepository.save(seller);
        log.info("Seller created successfully with id: {}", saved.getId());

        return (mapToResponse(saved));
    }

    public SellerResponse getById(Long id) {
        log.info("Searching seller with id: {}", id);
        return sellerRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.info("Fetching seller with id: {}", id);
                    return new ResourceNotFoundException("Seller with id: " + id + " not found");
                });
    }

    public List<SellerResponse> getAll() {
        log.info("Fetching all sellers");
        List<Seller> sellers = sellerRepository.findAll();
        log.info("Found {} sellers", sellers.size());
        return sellers.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public SellerResponse update(Long id, UpdateSellerRequest request) {
        log.info("Updating seller with id: {}", id);
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: Seller with id: {} not found", id);
                    return new ResourceNotFoundException("Seller with id: " + id + " not found");
                });

        Seller.SellerBuilder sellerBuilder = seller.toBuilder();
        if (request.name() != null && !request.name().isBlank()) {
            sellerBuilder.name(request.name());
        }

        if (request.contactInfo() != null && !request.contactInfo().isBlank()) {
            sellerBuilder.contactInfo(request.contactInfo());
        }

        Seller updated = sellerBuilder.build();
        log.info("Seller with id: {} updated successfully", id);

        return mapToResponse(sellerRepository.save(updated));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting seller with id: {}", id);
        if (!sellerRepository.existsById(id)) {
            log.warn("Delete failed: Seller with id: {} not found", id);
            throw new ResourceNotFoundException("Seller with id: " + id + " not found");
        }
        sellerRepository.deleteById(id);
        log.info("Seller with id: {} deleted successfully", id);
    }

    private Seller mapToEntity(CreateSellerRequest dto) {
        return Seller.builder()
                .name(dto.name())
                .contactInfo(dto.contactInfo())
                .build();
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
