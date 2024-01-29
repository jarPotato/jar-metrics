package com.changejar.example.controllers;

import com.changejar.example.services.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/test")
    ResponseEntity<String> testMapping() {
        exampleService.test();
        return ResponseEntity.ok("BRUH MOMINT");
    }
}
