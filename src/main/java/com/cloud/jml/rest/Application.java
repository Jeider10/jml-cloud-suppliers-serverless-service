package com.cloud.jml.rest;

import com.cloud.jml.banner.DynamicBanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Slf4j
@SpringBootApplication(scanBasePackages = "com.cloud.jml")
@EnableJpaRepositories(basePackages = "com.cloud.jml")
@EntityScan(basePackages = "com.cloud.jml")
public class Application {

    public static final String MICRO_NAME = "M i c r o  -  S u p p l i e r s";

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Application.class);
        app.setBanner(new DynamicBanner(MICRO_NAME));
        app.run(args);

        log.info("=======================================================");
        log.info("  ✅  jml-cloud-suppliers-serverless-service  ONLINE");
        log.info("  🚚  Servicio de gestion de proveedores");
        log.info("  🌐  Puerto : 1083  →  http://localhost:1083");
        log.info("  📊  Actuator: http://localhost:1083/actuator/health");
        log.info("=======================================================");
    }
}
