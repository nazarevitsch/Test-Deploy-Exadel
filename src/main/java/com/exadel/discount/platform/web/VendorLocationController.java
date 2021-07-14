package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import com.exadel.discount.platform.model.dto.update.VendorLocationBaseDto;
import com.exadel.discount.platform.service.RoleCheckService;
import com.exadel.discount.platform.service.VendorLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendor")
public class VendorLocationController {
    private final VendorLocationService locationService;
    private final RoleCheckService roleCheckService;

    @GetMapping(
            value = "/location",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<List<VendorLocationResponseDto>> getAllLocations(
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted
    ) {
        if (isDeleted && !roleCheckService.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(locationService.getAll(isDeleted), HttpStatus.OK);
    }

    @GetMapping(
            value = "/{vendorId}/location",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<Page<VendorLocationResponseDto>> getAllLocationsByFilters(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable UUID vendorId,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city
    ) {
        if (isDeleted && !roleCheckService.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(locationService.getAllByVendorId(page, size, vendorId, isDeleted, country, city), HttpStatus.OK);
    }

    @GetMapping(
            value = "/{vendorId}/location/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<VendorLocationResponseDto> getLocationById(@PathVariable UUID vendorId, @PathVariable UUID id) {
        VendorLocationResponseDto locationResponseDto = locationService.getById(vendorId, id);
        if (locationResponseDto.isDeleted() && !roleCheckService.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(locationService.getById(vendorId, id), HttpStatus.OK);
    }

    @PostMapping(
            value = "/{vendorId}/location",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> createLocation(@PathVariable UUID vendorId, @RequestBody VendorLocationBaseDto vendorLocationDto) {
        VendorLocationResponseDto savedLocation = locationService.save(vendorId, vendorLocationDto);
        return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
    }

    @PutMapping(
            value = "/{vendorId}/location/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> updateLocation(@PathVariable UUID vendorId, @PathVariable UUID id,
                                                                    @RequestBody VendorLocationBaseDto locationDto) {
        VendorLocationResponseDto updatedLocation = locationService.update(vendorId, id, locationDto);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    @DeleteMapping(
            value = "/{vendorId}/location/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> sendToArchive(@PathVariable UUID vendorId, @PathVariable UUID id) {
        locationService.toArchive(vendorId, id);
        VendorLocationResponseDto archivedLocation = locationService.getById(vendorId, id);
        return new ResponseEntity<>(archivedLocation, HttpStatus.OK);
    }
}
