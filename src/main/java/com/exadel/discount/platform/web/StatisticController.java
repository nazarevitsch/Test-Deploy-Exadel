package com.exadel.discount.platform.web;

import com.exadel.discount.platform.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/main_statistic")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getMainStatistic() {
        statisticService.getMainStatistic();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
