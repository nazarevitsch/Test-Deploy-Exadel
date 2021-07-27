package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.domain.User;
import com.exadel.discount.platform.model.dto.UserDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    final private ModelMapper modelMapper;

    public UserMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public UserDtoResponse entityToDto(User user) {
        return modelMapper.map(user, UserDtoResponse.class);
    }

    public List<UserDtoResponse> map(List<User> users) {
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
