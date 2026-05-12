package ru.shift.demo.simple_crm.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestContainerConfig.class)
@Transactional
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        Seller topSeller = sellerRepository.save(Seller.builder()
                .name("Top Seller")
                .contactInfo("top@example.com")
                .build());

        Seller averageSeller = sellerRepository.save(Seller.builder()
                .name("Average Seller")
                .contactInfo("avg@example.com")
                .build());

        transactionRepository.save(Transaction.builder()
                .seller(topSeller)
                .amount(new BigDecimal("1000.00"))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now().minusHours(1))
                .build());

        transactionRepository.save(Transaction.builder()
                .seller(topSeller)
                .amount(new BigDecimal("500.00"))
                .paymentType(PaymentType.CASH)
                .transactionDate(LocalDateTime.now().minusHours(2))
                .build());

        transactionRepository.save(Transaction.builder()
                .seller(averageSeller)
                .amount(new BigDecimal("300.00"))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now().minusHours(5))
                .build());
    }

    @Test
    void shouldReturnTopSeller() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/top")
                        .param("period", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Top Seller"))
                .andExpect(jsonPath("$.contactInfo").value("top@example.com"));
    }

    @Test
    void shouldReturnSellersUnderThreshold() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/sellers-under-threshold")
                        .param("start", LocalDateTime.now().minusDays(1).toString())
                        .param("end", LocalDateTime.now().plusDays(1).toString())
                        .param("maxTotalAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Average Seller"));
    }

    @Test
    void shouldReturnEmptyList_WhenAllSellersAboveThreshold() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/sellers-under-threshold")
                        .param("start", LocalDateTime.now().minusDays(1).toString())
                        .param("end", LocalDateTime.now().plusDays(1).toString())
                        .param("maxTotalAmount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnBadRequest_WhenPeriodIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/top")
                        .param("period", "INVALID_PERIOD"))
                .andExpect(status().isBadRequest());
    }
}