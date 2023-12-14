package com.sellbycar.marketplace.utilities.mapper;

import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR
        , uses = {CarMapper.class})
public interface AdvertisementMapper {
    @Mapping(source = "carDTO", target = "car")
    Advertisement toModel(AdvertisementDTO dto);

    @Mapping(source = "car", target = "carDTO")
    AdvertisementDTO toDTO(Advertisement model);

    Set<AdvertisementDTO> toDtoSet(Set<Advertisement> advertisements);
}
