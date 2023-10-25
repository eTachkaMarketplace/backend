package com.sellbycar.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sellbycar.marketplace")
public class MarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

}
