package com.sellbycar.marketplace.car;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDAO toModel(CarDTO carDTO);

    CarDTO toDTO(CarDAO car);
}
