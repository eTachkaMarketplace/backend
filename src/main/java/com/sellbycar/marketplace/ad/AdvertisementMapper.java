package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR
        , uses = {CarMapper.class})
public interface AdvertisementMapper {
    @Mapping(source = "carDTO", target = "car")
    @Mapping(target = "name", source = "ownerName")
    AdvertisementDAO toModel(AdvertisementDTO dto);

    @Mapping(source = "car", target = "carDTO")
    AdvertisementDTO toDTO(AdvertisementDAO model);

    Set<AdvertisementDTO> toDtoSet(Set<AdvertisementDAO> advertisements);
}
