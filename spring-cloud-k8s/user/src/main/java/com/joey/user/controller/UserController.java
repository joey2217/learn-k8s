package com.joey.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

        @GetMapping("/user")
        public String getUser(@RequestParam String id) {
            return "User" + id;
        }

    @GetMapping("/")
    public String hello() {
        return "Hello";
    }
}
