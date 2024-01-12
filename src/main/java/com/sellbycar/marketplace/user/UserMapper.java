package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.image.ImageDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "photo", qualifiedByName = "mapStringToImage")
    UserDTO toDTO(UserDAO user);

    @Named("mapStringToImage")
    default String mapStringToImage(ImageDAO image) {
        return image == null ? null : String.format("/image/%d", image.getId());
    }
}
