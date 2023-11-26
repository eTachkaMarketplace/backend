package com.sellbycar.marketplace.rest.dto;

import lombok.Data;

@Data
public class CarDTO {
    private String vinNumber;
    private String yearToCreate;
    private String carNumber;
    private Integer mileage;

}
