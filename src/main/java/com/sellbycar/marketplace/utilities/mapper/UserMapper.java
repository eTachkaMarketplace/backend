package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.models.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
}
