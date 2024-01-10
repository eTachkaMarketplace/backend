package com.sellbycar.marketplace.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDAO toEntity(UserDTO userDTO);

    UserDTO toDTO(UserDAO user);
}
