package ru.shift.demo.simple_crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shift.demo.simple_crm.dto.request.CreateTransactionRequest;
import ru.shift.demo.simple_crm.dto.response.TransactionResponse;
import ru.shift.demo.simple_crm.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse register(@Valid @RequestBody CreateTransactionRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(@RequestParam(required = false) Long sellerId) {
        if (sellerId != null) {
            return service.getTransactionsBySellerId(sellerId);
        }
        return service.getAll();
    }
}
