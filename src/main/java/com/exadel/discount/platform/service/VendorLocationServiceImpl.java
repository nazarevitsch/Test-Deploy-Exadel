package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorLocationMapper;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.VendorLocationDto;
import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import com.exadel.discount.platform.repository.VendorLocationRepository;
import com.exadel.discount.platform.repository.VendorRepository;
import com.exadel.discount.platform.service.interfaces.VendorLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorLocationServiceImpl implements VendorLocationService {
    private final VendorLocationRepository locationRepository;
    private final VendorRepository vendorRepository;
    private final VendorLocationMapper mapper;

    @Override
    public List<VendorLocationResponseDto> getAll() {
        return mapper.mapList(locationRepository.findAll(), VendorLocationResponseDto.class);
    }

    @Override
    public VendorLocationResponseDto save(VendorLocationDto vendorLocationDto) {
        Vendor vendor = vendorRepository.getById(vendorLocationDto.getVendorId());
        if (vendor.isDeleted()) {
            throw new DeletedException("Cannot create Vendor Location with deleted Vendor");
        }
        VendorLocation location = locationRepository.save(mapper.dtoToEntity(vendorLocationDto));
        return mapper.entityToResponseDto(location);
    }

    @Override
    public VendorLocationResponseDto getById(UUID id) {
        VendorLocation location = locationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor location with id " + id +
                        " was not found"));
        return mapper.entityToResponseDto(location);
    }

    @Override
    public VendorLocationResponseDto update(UUID id, VendorLocationDto vendorLocationDto) {
        VendorLocation location = locationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor location with id " + id +
                        " was not found"));

        if (location.isDeleted()) {
            throw new DeletedException("Cannot update deleted Vendor Location with id" + id
            );
        }

        mapper.update(vendorLocationDto, location);
        location = locationRepository.save(location);
        return mapper.entityToResponseDto(location);
    }

    @Override
    public void toArchive(UUID id) {
        boolean isExists = locationRepository.existsById(id);
        if (isExists) {
            locationRepository.deleteById(id);
        } else {
            throw new NotFoundException("Vendor location with id " + id +
                    " was not found");
        }
    }
}
