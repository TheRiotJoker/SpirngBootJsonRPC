package de.theriotjoker.mathfactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MathFactoryApplication {

    public static void main(String[] args) {
        log.info("Starting service... v0.0.1");
        SpringApplication.run(MathFactoryApplication.class, args);
    }

}
