package com.sellbycar.marketplace.rest.mapper;

import com.sellbycar.marketplace.persistance.model.User;
import com.sellbycar.marketplace.rest.dto.UserDTO;
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
