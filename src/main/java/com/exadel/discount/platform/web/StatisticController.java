package com.exadel.discount.platform.web;

import com.exadel.discount.platform.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/main_statistic")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getMainStatistic() {
        statisticService.getMainStatistic();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<?> getUserHistory(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return new ResponseEntity<>(statisticService.getUserHistory(page, size), HttpStatus.OK);
    }
}
