package com.sellbycar.marketplace.image;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Value("${server.url}")
    private String serverUrl;

    abstract ImageDTO toDTO(ImageDAO dao);

    public String toString(ImageDAO dao) {
        return dao == null ? null : String.format("%s/image/%d", serverUrl, dao.getId());
    }

    public ImageDAO toDAO(String string) {
        return null;
    }
}
