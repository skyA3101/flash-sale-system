package com.lightning.orderservice.controller;


import com.lightning.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/stock/{productId}")
    public Integer getStock(@PathVariable Long productId){
        return orderService.getCurrentStock(productId);
    }

    @PutMapping("/add-stock/{productId}/{quantity}")
    public String addStock(@PathVariable Long productId,@PathVariable int quantity){
        return orderService.addStock(productId,quantity);
    }

    @PostMapping("/{productId}")
    public String buy(@PathVariable Long productId) {
        return orderService.placeOrder(productId);
    }

}
