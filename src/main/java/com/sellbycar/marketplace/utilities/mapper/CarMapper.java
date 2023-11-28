package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.entities.Car;
import com.sellbycar.marketplace.models.dto.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "vinNumber", ignore = true)
    Car toModel(CarDTO carDTO);

    CarDTO toDTO(Car car);
}
