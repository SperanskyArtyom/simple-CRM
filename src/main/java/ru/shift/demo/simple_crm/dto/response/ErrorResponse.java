package ru.shift.demo.simple_crm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        Map<String, String> errors
) {
}
