package com.example.oms.service;

import com.example.oms.model.Order;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();

    public String placeOrder(int bookId,int quantity);


}
