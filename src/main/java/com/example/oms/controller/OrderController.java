package com.example.oms.controller;

import com.example.oms.model.Order;
import com.example.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oms/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderServiceImpl")
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestParam int bookId,@RequestParam int quantity){
        String res = orderService.placeOrder(bookId,quantity);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
}
