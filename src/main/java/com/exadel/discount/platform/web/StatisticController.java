package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.UsedDiscountDtoResponse;
import com.exadel.discount.platform.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
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
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate
             ) {
        return new ResponseEntity<>(statisticService.getUserHistory(page, size, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/used_discount/history")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<?> getUsedDiscountHistory(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
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

    @GetMapping("/used_discount/history/file")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public void demo(HttpServletResponse response,
                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
                     @RequestParam(value = "categoryId", required = false) UUID categoryId,
                     @RequestParam(value = "subCategoryId", required = false) UUID subCategoryId,
                     @RequestParam(value = "vendorId", required = false) UUID vendorId,
                     @RequestParam(value = "vendorId", required = false) UUID userId,
                     @RequestParam(value = "country", required = false) String country,
                     @RequestParam(value = "city", required = false) String city
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "statistic" + new Date().toString() + ".csv"));

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = { "Id", "Name", "FullDescription", "UsageDate"};
        csvWriter.writeHeader(header);

        Page<UsedDiscountDtoResponse> a = statisticService.getUsedDiscountHistory(
                0, 10, startDate, endDate, categoryId, subCategoryId, vendorId, userId, country, city);

        for (UsedDiscountDtoResponse usedDiscountDtoResponse : a.toList()) {
            csvWriter.write(usedDiscountDtoResponse, header);
        }
        csvWriter.close();
    }
}
