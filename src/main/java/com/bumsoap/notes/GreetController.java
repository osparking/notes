package com.bumsoap.notes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {
    @GetMapping("/greet")
    public String greet() {
        return "안녕하세요";
    }

    @GetMapping("/goodbye")
    public String goodbye() {
        return "잘가세요";
    }
}
