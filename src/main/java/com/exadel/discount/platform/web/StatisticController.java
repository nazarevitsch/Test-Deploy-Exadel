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

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/main_statistic")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getMainStatistic() {
        return new ResponseEntity<>( statisticService.getMainStatistic(), HttpStatus.OK);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<?> getUserHistory(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startDate", required = false) ZonedDateTime startDate,
            @RequestParam(value = "endDate", required = false) ZonedDateTime endDate
             ) {
        return new ResponseEntity<>(statisticService.getUserHistory(page, size, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/used_discount/history")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getUsedDiscountHistory(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startDate", required = false) ZonedDateTime startDate,
            @RequestParam(value = "endDate", required = false) ZonedDateTime endDate,
            @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @RequestParam(value = "subCategoryId", required = false) UUID subCategoryId,
            @RequestParam(value = "vendorId", required = false) UUID vendorId,
            @RequestParam(value = "vendorId", required = false) UUID userId,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city
    ) {
        return new ResponseEntity<>(statisticService.getUsedDiscountHistory(
                page, size, startDate, endDate, categoryId, subCategoryId, vendorId, userId, country, city), HttpStatus.OK);
    }
}
