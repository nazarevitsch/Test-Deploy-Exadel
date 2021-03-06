package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.VendorLocationDto;
import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import com.exadel.discount.platform.service.interfaces.VendorLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendor/location")
public class VendorLocationController {
    private final VendorLocationService locationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<List<VendorLocationResponseDto>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> getLocationById(@PathVariable UUID id) {
        return new ResponseEntity<>(locationService.getById(id), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> createLocation(@RequestBody VendorLocationDto vendorLocationDto) {
        VendorLocationResponseDto savedLocation = locationService.save(vendorLocationDto);
        return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationResponseDto> updateLocation(@PathVariable UUID id,
                                                                        @RequestBody VendorLocationDto locationDto) {
        VendorLocationResponseDto updatedLocation = locationService.update(id, locationDto);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorLocationDto> sendToArchive(@PathVariable UUID id) {
        locationService.toArchive(id);
        VendorLocationResponseDto archivedLocation = locationService.getById(id);
        return new ResponseEntity<>(archivedLocation, HttpStatus.OK);
    }
}
