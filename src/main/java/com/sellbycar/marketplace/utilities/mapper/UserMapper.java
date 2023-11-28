package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.models.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "phone", ignore = true)
    User toModel(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "phone", ignore = true)
    UserDTO toDTO(User user);
}
