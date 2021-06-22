package com.exadel.discount.platform.service;

import com.exadel.discount.platform.converter.VendorMapper;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.dto.VendorDto;
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
    public List<VendorDto> getAll() {
        return mapper.mapList(vendorRepository.findAll(), VendorDto.class);
    }

    @Override
    public VendorDto save(VendorDto vendorDto) {
        Vendor vendor = vendorRepository.save(mapper.dtoToEntity(vendorDto));
        return mapper.entityToDto(vendor);
    }

    @Override
    public VendorDto getById(UUID id) {
        Vendor vendor = vendorRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor with id " + id +
                        " was not found"));
        return mapper.entityToDto(vendor);
    }

    @Override
    public VendorDto update(UUID id, VendorDto vendorDto) {
        Vendor vendor = vendorRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor with id " + id +
                        " was not found"));
        vendor = mapper.dtoToEntity(vendorDto);
        vendor = vendorRepository.save(vendor);
        return mapper.entityToDto(vendor);
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
