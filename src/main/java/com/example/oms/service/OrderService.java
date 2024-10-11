package com.example.oms.service;

import com.example.oms.model.Book;
import com.example.oms.model.Order;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();

    public List<Book> getBooksByCategory(String category);
    public List<Book> getBooksByAuthorName(String authorName);

    public String placeOrderByCategory(String category,int bookId,int quantity);
    public String placeOrderByAuthorName(String authorName,int bookId,int quantity);


}
