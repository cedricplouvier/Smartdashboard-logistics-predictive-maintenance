package com.example.demo.SmartDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SmartDashboard {

    public static void main(String[] args) {
        SpringApplication.run(SmartDashboard.class, args);
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "cedric") String name) {
        return String.format("Hello %s!", name);
    }
}
