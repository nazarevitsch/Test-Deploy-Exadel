package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.VendorDto;
import com.exadel.discount.platform.model.dto.VendorResponseDto;
import com.exadel.discount.platform.service.interfaces.VendorService;
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
@RequestMapping("/vendor")
public class VendorController {
    private final VendorService vendorService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<List<VendorResponseDto>> getAllVendors() {
        return new ResponseEntity<>(vendorService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorResponseDto> getVendorById(@PathVariable UUID id) {
        return new ResponseEntity<>(vendorService.getById(id), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorResponseDto> createVendor(@RequestBody VendorDto vendorDto) {
        VendorResponseDto savedVendor = vendorService.save(vendorDto);
        return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorResponseDto> updateVendor(@PathVariable UUID id,
                                                              @RequestBody VendorDto vendorDto) {
        VendorResponseDto updatedVendor = vendorService.update(id, vendorDto);
        return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<VendorResponseDto> sendToArchive(@PathVariable UUID id) {
        vendorService.toArchive(id);
        VendorResponseDto archivedVendor = vendorService.getById(id);
        return new ResponseEntity<>(archivedVendor, HttpStatus.OK);
    }
}
