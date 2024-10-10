package com.example.oms.service;

import com.example.oms.model.Book;
import com.example.oms.model.Order;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();

    public List<Book> getBooksByCategory(String category);

    public String placeOrder(String category,int bookId,int quantity);


}
