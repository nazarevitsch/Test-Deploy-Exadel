package com.exadel.discount.platform.converter;

import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.VendorLocationDto;
import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendorLocationMapper {
    private ModelMapper modelMapper;

    public VendorLocationMapper() {
        modelMapper = new ModelMapper();
    }

    public VendorLocationDto entityToDto(VendorLocation vendorLocation) {
        return modelMapper.map(vendorLocation, VendorLocationDto.class);
    }

    public VendorLocationResponseDto entityToResponseDto(VendorLocation vendorLocation) {
        return modelMapper.map(vendorLocation, VendorLocationResponseDto.class);
    }

    public void update(VendorLocationDto vendorLocationDto, VendorLocation vendorLocation) {
        modelMapper.map(vendorLocationDto, vendorLocation);
    }

    public VendorLocation dtoToEntity(VendorLocationDto locationDto) {
        return modelMapper.map(locationDto, VendorLocation.class);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
