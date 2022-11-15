package com.joey.order.controller;

import com.joey.order.client.UserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    final UserClient userClient;

    public OrderController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/order")
    public String order(@RequestParam String id) {
        String user = userClient.getUser(id);
        return "order" + id + "==user" + id;
    }


    @GetMapping("/")
    public String hello() {
        return "Hello order";
    }
}
