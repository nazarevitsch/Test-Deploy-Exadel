package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorMapper;
import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.dto.VendorDto;
import com.exadel.discount.platform.model.dto.VendorResponseDto;
import com.exadel.discount.platform.repository.VendorRepository;
import com.exadel.discount.platform.service.interfaces.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper mapper;


    @Override
    public List<VendorResponseDto> getAll(boolean isDeleted) {
        return mapper.mapList(vendorRepository.findAllByDeleted(isDeleted), VendorResponseDto.class);
    }

    @Override
    public VendorResponseDto save(VendorDto vendorDto) {
        Vendor vendor = vendorRepository.save(mapper.dtoToEntity(vendorDto));
        return mapper.entityToResponseDto(vendor);
    }

    @Override
    public VendorResponseDto getById(UUID id) {
        Vendor vendor = vendorRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor with id " + id +
                        " was not found"));
        return mapper.entityToResponseDto(vendor);
    }

    @Override
    public VendorResponseDto update(UUID id, VendorDto vendorDto) {
        Vendor vendor = vendorRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor with id " + id +
                        " was not found"));

        if (vendor.isDeleted()) {
            throw new DeletedException("Cannot update deleted Vendor with id" + id
            );
        }
        mapper.updateVendor(vendorDto, vendor);
        vendor = vendorRepository.save(vendor);
        return mapper.entityToResponseDto(vendor);
    }

    @Override
    public void toArchive(UUID id) {
        boolean isExists = vendorRepository.existsById(id);
        if (isExists) {
            vendorRepository.deleteById(id);
        } else {
            throw new NotFoundException("Vendor with id " + id +
                    " was not found");
        }
    }
}
