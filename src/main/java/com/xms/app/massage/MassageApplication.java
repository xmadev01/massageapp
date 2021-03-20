package com.xms.app.massage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.xms.app.massage.repository")
@EntityScan("com.xms.app.massage.model")
public class MassageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MassageApplication.class, args);
    }

}
