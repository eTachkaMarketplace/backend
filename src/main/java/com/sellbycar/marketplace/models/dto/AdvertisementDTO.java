package com.sellbycar.marketplace.models.dto;

import com.sellbycar.marketplace.models.entities.Region;
import lombok.Data;

@Data
public class AdvertisementDTO {
    private long id;
    private String description;
    private String name;
    private int price;
    private Region region;
    private CarDTO carDTO;
}
