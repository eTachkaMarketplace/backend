package com.sellbycar.marketplace;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.DockerClientFactory;

@Slf4j
public class DevelopmentLauncher {

    public static void main(String[] args) {
        exitIfDockerNotRunning();
        MarketplaceApplication.main(args);
    }

    private static void exitIfDockerNotRunning() {
        if (!DockerClientFactory.instance().isDockerAvailable()) {
            log.error("Install and run Docker to boot the development environment.");
            Runtime.getRuntime().exit(1);
        }
    }
}
