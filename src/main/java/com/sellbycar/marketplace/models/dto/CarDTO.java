package com.sellbycar.marketplace.models.dto;

import com.sellbycar.marketplace.models.entities.*;
import lombok.Data;

@Data
public class CarDTO {
    private CarMark carMark;
    private String carNumber;
    private String yearToCreate;
    private Integer mileage;
    private BodyType bodyType;
    private Engine engine;
    private DriveType driveType;
    private Transmission transmission;
    private TechnicalState technicalState;
    private Color color;
    private String vinNumber;
}
