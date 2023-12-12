package com.sellbycar.marketplace.models.dto;

import com.sellbycar.marketplace.models.entities.City;
import com.sellbycar.marketplace.models.entities.Region;
import lombok.Data;

@Data
public class AdvertisementDTO {
    private Long id;
    private String description;
    private String name;
    private Integer price;
    private Region region;
    private CarDTO carDTO;
}
