package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AdvertisementDTO {

    private long id;
    private String description;
    private String region;
    private String category;
    private CarDTO car;
    private String contactName;
    private String contactPhone;
    private Boolean isActive;
    private String previewImage;
    private List<String> images;
    private Instant createdTimestamp;
}
