package com.sellbycar.marketplace.image;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO toDTO(ImageDAO dao);

    default String toString(ImageDAO dao) {
        return dao == null ? null : String.format("/image/%d", dao.getId());
    }

    default ImageDAO toDAO(String string) {
        return null;
    }
}
