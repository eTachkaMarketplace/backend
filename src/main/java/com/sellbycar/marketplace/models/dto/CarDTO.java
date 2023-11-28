package com.sellbycar.marketplace.models.dto;

import lombok.Data;

@Data
public class CarDTO {
    private String vinNumber;
    private String yearToCreate;
    private String carNumber;
    private Integer mileage;

}
