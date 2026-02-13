package com.yappa.customerapi;

import org.springframework.boot.SpringApplication;

public class TestCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(CustomerApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
