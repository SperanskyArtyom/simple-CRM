package ru.shift.demo.simple_crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shift.demo.simple_crm.dto.request.CreateSellerRequest;
import ru.shift.demo.simple_crm.dto.request.UpdateSellerRequest;
import ru.shift.demo.simple_crm.dto.response.SellerResponse;
import ru.shift.demo.simple_crm.service.SellerService;

import java.util.List;

@RestController
@RequestMapping("api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResponse register(@Valid @RequestBody CreateSellerRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public SellerResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<SellerResponse> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}")
    public SellerResponse updateById(@PathVariable Long id, @Valid @RequestBody UpdateSellerRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
