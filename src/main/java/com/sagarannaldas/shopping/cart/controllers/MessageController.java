package com.sagarannaldas.shopping.cart.controllers;

import com.sagarannaldas.shopping.cart.entities.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @GetMapping("/hello")
    public Message sayHello() {
        return new Message("Hello World!");
    }
}
