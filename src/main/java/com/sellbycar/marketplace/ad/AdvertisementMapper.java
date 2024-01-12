package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarMapper;
import com.sellbycar.marketplace.image.ImageDAO;
import com.sellbycar.marketplace.image.ImageMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {CarMapper.class, ImageMapper.class})
public interface AdvertisementMapper {

    AdvertisementDAO toDAO(AdvertisementDTO dto);

    AdvertisementDTO toDTO(AdvertisementDAO model);

    Set<AdvertisementDTO> toDtoSet(Set<AdvertisementDAO> advertisements);
}
