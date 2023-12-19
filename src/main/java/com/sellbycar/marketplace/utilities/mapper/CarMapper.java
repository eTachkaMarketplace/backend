package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.dto.CarDTO;
import com.sellbycar.marketplace.models.entities.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toModel(CarDTO carDTO);

    CarDTO toDTO(Car car);
}
