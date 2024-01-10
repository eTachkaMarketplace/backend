package com.sellbycar.marketplace;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DevelopmentLauncher {

    public static void main(String[] args) {
        checkDocker();
        MarketplaceApplication.main(args);
    }

    private static void checkDocker() {
        File dockerSocket = new File("/var/run/docker.sock");
        if (!dockerSocket.exists()) {
            log.error("Install and run Docker to boot the development environment.");
            Runtime.getRuntime().exit(1);
        }
    }
}
