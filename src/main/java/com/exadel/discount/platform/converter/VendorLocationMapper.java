package com.exadel.discount.platform.converter;

import com.exadel.discount.platform.model.VendorLocation;
import com.exadel.discount.platform.model.dto.VendorLocationDto;
import com.exadel.discount.platform.model.dto.VendorLocationResponseDto;
import com.exadel.discount.platform.model.dto.update.VendorLocationBaseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendorLocationMapper {
    private ModelMapper modelMapper;

    public VendorLocationMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public VendorLocationDto entityToDto(VendorLocation vendorLocation) {
        return modelMapper.map(vendorLocation, VendorLocationDto.class);
    }

    public VendorLocationResponseDto entityToResponseDto(VendorLocation vendorLocation) {
        VendorLocationResponseDto responseDto = modelMapper.map(vendorLocation, VendorLocationResponseDto.class);
        responseDto.setVendorId(vendorLocation.getVendor().getId());
        return responseDto;
    }

    public void update(VendorLocationBaseDto vendorLocationDto, VendorLocation vendorLocation) {
        modelMapper.map(vendorLocationDto, vendorLocation);
    }

    public VendorLocation dtoToEntity(VendorLocationDto locationDto) {
        return modelMapper.map(locationDto, VendorLocation.class);
    }

    public List<VendorLocationResponseDto> mapList(List<VendorLocation> locations){
        return locations.stream().map(this::entityToResponseDto).collect(Collectors.toList());
    }
}
