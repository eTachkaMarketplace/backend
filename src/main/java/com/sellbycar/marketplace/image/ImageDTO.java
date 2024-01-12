package com.sellbycar.marketplace.image;

import lombok.Data;

import java.time.Instant;

@Data
public class ImageDTO {

    private Long id;
    private String name;
    private Long size;
    private String contentType;
    private Instant createdTimestamp;
}
