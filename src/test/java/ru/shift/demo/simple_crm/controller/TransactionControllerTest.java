package ru.shift.demo.simple_crm.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.shift.demo.simple_crm.config.TestContainerConfig;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.domain.entity.Transaction;
import ru.shift.demo.simple_crm.domain.entity.constants.PaymentType;
import ru.shift.demo.simple_crm.repository.SellerRepository;
import ru.shift.demo.simple_crm.repository.TransactionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestContainerConfig.class)
@Transactional
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldReturnTransactionsBySeller() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder()
                .name("Test Seller").contactInfo("test@mail.ru").build());

        transactionRepository.save(
                Transaction.builder()
                        .seller(seller)
                        .amount(new BigDecimal("500.00"))
                        .paymentType(PaymentType.CARD)
                        .transactionDate(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(get("/api/v1/transactions")
                        .param("sellerId", seller.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].amount").value(500.00));
    }
}