package ru.shift.demo.simple_crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSellerRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,
        @NotBlank(message = "Contact information is required")
        @Size(max = 255, message = "Contact information must be less than 255 characters")
        String contactInfo
) { }
