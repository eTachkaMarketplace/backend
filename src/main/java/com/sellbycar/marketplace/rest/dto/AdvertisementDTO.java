package com.sellbycar.marketplace.rest.dto;

import com.sellbycar.marketplace.persistance.model.User;
import lombok.Data;

@Data
public class AdvertisementDTO {
    private Long id;

    private String description;

    private String name;

    private Integer price;

    private boolean change = false;

    private boolean bargain = false;

    private boolean crashed = false;

    private CarDTO carDTO;

}
