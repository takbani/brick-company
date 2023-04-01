package com.caci.technical.test.bricks.controller;

import com.caci.technical.test.bricks.exceptions.OrderNotFoundException;
import com.caci.technical.test.bricks.model.Order;
import com.caci.technical.test.bricks.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> submitOrder(@RequestBody Order order) {
        Long orderReference = orderService.submitOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderReference);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> submitOrder(@PathVariable Long id) throws OrderNotFoundException {
        Optional<Order> orderReference = orderService.findOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderReference.get());
    }
}
