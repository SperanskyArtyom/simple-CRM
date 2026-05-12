package ru.shift.demo.simple_crm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.domain.entity.Transaction;
import ru.shift.demo.simple_crm.dto.request.CreateTransactionRequest;
import ru.shift.demo.simple_crm.dto.response.TransactionResponse;
import ru.shift.demo.simple_crm.exception.ResourceNotFoundException;
import ru.shift.demo.simple_crm.repository.SellerRepository;
import ru.shift.demo.simple_crm.repository.TransactionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        log.info("Creating transaction for seller id: {}, amount: {}", request.sellerId(), request.amount());

        // Проверка существование продавца с id, переданным в dto
        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> {
                    log.warn("Create transaction failed: Seller with id {} not found", request.sellerId());
                    return new ResourceNotFoundException("Seller with id: " + request.sellerId() + " not found");
                });

        Transaction transaction = mapToEntity(request, seller);
        Transaction saved = transactionRepository.save(transaction);

        log.info("Transaction created successfully with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    public TransactionResponse getById(Long id) {
        log.info("Fetching transaction with id: {}", id);
        return transactionRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.warn("Transaction with id: {} not found", id);
                    return new ResourceNotFoundException("Transaction with id: " + id + " not found");
                });
    }

    public List<TransactionResponse> getAll() {
        log.info("Fetching all transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        log.info("Found {} transactions", transactions.size());
        return transactions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TransactionResponse> getTransactionsBySellerId(Long sellerId) {
        log.info("Fetching transactions by seller");
        if (!sellerRepository.existsById(sellerId)) {
            log.warn("Fetching transactions by seller failed: Seller with id {} not found", sellerId);
            throw new ResourceNotFoundException("Seller with id: " + sellerId + " not found");
        }
        List<Transaction> transactions = transactionRepository.findAllBySellerId(sellerId);
        log.info("Found {} transactions of seller with id {}", transactions.size(), sellerId);
        return transactions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Transaction mapToEntity(CreateTransactionRequest dto, Seller seller) {
        return Transaction.builder()
                .seller(seller)
                .amount(dto.amount())
                .paymentType(dto.paymentType())
                .build();
    }

    private TransactionResponse mapToResponse(Transaction entity) {
        return new TransactionResponse(
                entity.getId(),
                entity.getSeller().getId(),
                entity.getAmount(),
                entity.getPaymentType(),
                entity.getTransactionDate()
        );
    }
}
