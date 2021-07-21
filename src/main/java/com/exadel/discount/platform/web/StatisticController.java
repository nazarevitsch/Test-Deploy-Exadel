package com.exadel.discount.platform.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/statistic")
public class StatisticController {

    @GetMapping("/main_statistic")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getMainStatistic() {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
