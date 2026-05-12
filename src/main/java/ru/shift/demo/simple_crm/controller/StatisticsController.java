package ru.shift.demo.simple_crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.shift.demo.simple_crm.dto.constants.PeriodType;
import ru.shift.demo.simple_crm.dto.request.SellerStatisticsRequest;
import ru.shift.demo.simple_crm.dto.response.SellerResponse;
import ru.shift.demo.simple_crm.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService service;

    @GetMapping("/top")
    public SellerResponse getTopSeller(@RequestParam PeriodType period) {
        return service.getMostProductiveSeller(period);
    }

    @GetMapping("/sellers-under-threshold")
    public List<SellerResponse> getSellersUnderThreshold(@Valid SellerStatisticsRequest request) {
        return service.getSellersUnderThreshold(request);
    }
}
