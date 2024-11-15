package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sp.fc"})
public class CustomLoginApp {


    public static void main(String[] args) {
        SpringApplication.run(CustomLoginApp.class, args);
    }

}
