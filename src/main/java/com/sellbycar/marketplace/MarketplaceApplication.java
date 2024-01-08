package com.sellbycar.marketplace;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketplaceApplication {

    public static void main(String[] args) {
        setupSentry();
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    public static void setupSentry() {
        String dsn = System.getenv("SENTRY_DSN");
        Sentry.init(options -> {
            options.setDsn(dsn);
            options.setEnabled(dsn != null);
            options.setTracesSampleRate(1.0);
        });
    }
}
