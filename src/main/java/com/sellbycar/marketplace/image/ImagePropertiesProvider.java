package com.sellbycar.marketplace.image;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class ImagePropertiesProvider {

    @Value("${server.url}")
    private String serverUrl;
}
