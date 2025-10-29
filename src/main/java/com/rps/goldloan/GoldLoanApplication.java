package com.rps.goldloan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class GoldLoanApplication {

    public static void main(String[] args) {

        SpringApplication.run(GoldLoanApplication.class, args);
    }

}
