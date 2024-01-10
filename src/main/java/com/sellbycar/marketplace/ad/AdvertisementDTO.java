package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.car.CarDTO;
import com.sellbycar.marketplace.car.dao.Region;
import lombok.Data;

@Data
public class AdvertisementDTO {
    private long id;
    private String description;
    private String ownerName;
    private String ownerPhone;
    private int price;
    private Region region;
    private CarDTO carDTO;
}
