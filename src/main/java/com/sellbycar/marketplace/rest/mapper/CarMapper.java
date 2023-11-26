package com.sellbycar.marketplace.rest.mapper;

import com.sellbycar.marketplace.persistance.model.Car;
import com.sellbycar.marketplace.rest.dto.CarDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "vinNumber", ignore = true)
    Car toModel(CarDTO carDTO);

    CarDTO toDTO(Car car);
}
