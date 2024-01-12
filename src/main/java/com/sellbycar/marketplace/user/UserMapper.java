package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.image.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface UserMapper {

    UserDTO toDTO(UserDAO user);
}
