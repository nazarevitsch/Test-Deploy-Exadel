package com.exadel.discount.platform.converter;

import com.exadel.discount.platform.model.Vendor;
import com.exadel.discount.platform.model.dto.VendorDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendorMapper {
    ModelMapper modelMapper;

    public VendorMapper() {
        modelMapper = new ModelMapper();
    }

    public VendorDto entityToDto(Vendor vendor) {
        return modelMapper.map(vendor, VendorDto.class);
    }

    public Vendor dtoToEntity(VendorDto vendorDto) {
       return modelMapper.map(vendorDto, Vendor.class);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
