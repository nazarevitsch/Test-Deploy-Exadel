package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorLocationMapper;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.VendorLocationDto;
import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import com.exadel.discount.platform.model.dto.update.VendorLocationBaseDto;
import com.exadel.discount.platform.repository.VendorLocationRepository;
import com.exadel.discount.platform.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorLocationService {
    private final VendorLocationRepository locationRepository;
    private final VendorRepository vendorRepository;
    private final VendorLocationMapper mapper;

    public List<VendorLocationResponseDto> getAllByVendorId(UUID vendorId, boolean isDeleted) {
        return mapper.mapList(locationRepository.findAllByVendorIdAndDeleted(vendorId, isDeleted));
    }

    public VendorLocationResponseDto save(UUID vendorId, VendorLocationDto vendorLocationDto) {
        Vendor vendor = vendorRepository.getById(vendorId);
        if (vendor.isDeleted()) {
            throw new DeletedException("Cannot create Vendor Location with deleted Vendor");
        }
        VendorLocation location = mapper.dtoToEntity(vendorLocationDto);
        location.setVendor(vendor);
        location = locationRepository.save(location);
        return mapper.entityToResponseDto(location);
    }

    public VendorLocationResponseDto getById(UUID vendorId, UUID id) {
        VendorLocation location = locationRepository.findByVendorIdAndId(vendorId, id)
                .orElseThrow(() -> new NotFoundException("Vendor location with id " + id +
                " was not found"));
            return mapper.entityToResponseDto(location);
    }

    public VendorLocationResponseDto update(UUID vendorId, UUID id, VendorLocationBaseDto vendorLocationDto) {
        VendorLocation location = locationRepository
                .findByVendorIdAndId(vendorId, id).orElseThrow(() -> new NotFoundException("Vendor location with id " + id +
                " was not found"));
            if (location.isDeleted()) {
                throw new DeletedException("Cannot update deleted Vendor Location with id" + id
                );
            }

            mapper.update(vendorLocationDto, location);
            location = locationRepository.save(location);
            return mapper.entityToResponseDto(location);
    }

    public void toArchive(UUID vendorId, UUID id) {
        boolean isExists = locationRepository.existsByVendorIdAndId(vendorId, id);
        if (isExists) {
            locationRepository.deleteById(id);
        } else {
            throw new NotFoundException("Vendor location with id " + id +
                    " was not found");
        }
    }
}
