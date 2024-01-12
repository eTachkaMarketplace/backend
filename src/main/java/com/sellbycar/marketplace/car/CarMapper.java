package com.sellbycar.marketplace.car;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDTO toDTO(CarDAO dao);
}
