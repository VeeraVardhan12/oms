package com.example.oms.controller;

import com.example.oms.model.Book;
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

    @GetMapping("/bookSearch/category/{category}")
    public List<Book> getBooksByCategory(@PathVariable String category){
        return orderService.getBooksByCategory(category);
    }

    @PostMapping("/placeOrder/{category}")
    public ResponseEntity<String> placeOrder(@PathVariable String category,@RequestParam int bookId,@RequestParam int quantity){
        String res = orderService.placeOrder(category,bookId,quantity);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
}
