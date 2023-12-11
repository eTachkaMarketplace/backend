package com.sellbycar.marketplace.models.dto;

import lombok.Data;

@Data
public class AdvertisementDTO {
    private Long id;
    private String description;
    private String name;
    private Integer price;
    private CarDTO carDTO;
}
