package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.dto.UserDTO;
import com.sellbycar.marketplace.models.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
}
