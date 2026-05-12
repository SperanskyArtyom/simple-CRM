package ru.shift.demo.simple_crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.shift.demo.simple_crm.config.TestContainerConfig;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.dto.request.CreateSellerRequest;
import ru.shift.demo.simple_crm.dto.request.UpdateSellerRequest;
import ru.shift.demo.simple_crm.repository.SellerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestContainerConfig.class)
@Transactional
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Test
    void shouldRegisterSeller() throws Exception {
        var request = new CreateSellerRequest("Test Seller", "new@example.com");

        mockMvc.perform(post("/api/v1/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Seller"))
                .andExpect(jsonPath("$.contactInfo").value("new@example.com"));
    }

    @Test
    void shouldGetSellerById() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder()
                .name("John")
                .contactInfo("john@mail.ru")
                .build());

        mockMvc.perform(get("/api/v1/sellers/{id}", seller.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(seller.getId()))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldGetAllSellers() throws Exception {
        sellerRepository.save(Seller.builder().name("S1").contactInfo("c1").build());
        sellerRepository.save(Seller.builder().name("S2").contactInfo("c2").build());

        mockMvc.perform(get("/api/v1/sellers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateSeller() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder()
                .name("Old Name")
                .contactInfo("old@mail.ru")
                .build());

        var updateRequest = new UpdateSellerRequest("Updated Name", null);

        mockMvc.perform(patch("/api/v1/sellers/{id}", seller.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.contactInfo").value("old@mail.ru"));
    }

    @Test
    void shouldDeleteSeller() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder()
                .name("To Delete")
                .contactInfo("delete@mail.ru")
                .build());

        mockMvc.perform(delete("/api/v1/sellers/{id}", seller.getId()))
                .andExpect(status().isNoContent());

        assert !sellerRepository.existsById(seller.getId());
    }
}