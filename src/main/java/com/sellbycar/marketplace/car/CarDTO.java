package com.sellbycar.marketplace.car;

import com.sellbycar.marketplace.car.dao.*;
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
