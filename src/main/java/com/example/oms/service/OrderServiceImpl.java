package com.example.oms.service;

import com.example.oms.model.Book;
import com.example.oms.model.Order;
import com.example.oms.model.QuantityRequest;
import com.example.oms.repository.OrderRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final String lmsUri = "http://localhost:8080/api/books?bookId=";

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }
    public String placeOrder(int bookId, int quantity){
        // Create a parameterized type reference to handle the response
        ParameterizedTypeReference<List<Book>> typeRef = new ParameterizedTypeReference<>() {};

        // Make the GET request and retrieve the response entity
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                lmsUri + bookId,
                HttpMethod.GET,
                null,
                typeRef
        );

        // Retrieve the list of books from the response
        List<Book> books = responseEntity.getBody();

        if (books == null || books.isEmpty()) {
            return "Book not found with ID: " + bookId;
        }

        // Get the first book object (assuming the API returns a single book wrapped in an array)
        Book book = books.get(0);

        // Check if the book is null or if there isn't enough quantity
        if (book == null || book.getQuantity() < quantity) {
            return "Book unavailable or insufficient stock";
        }

        // Create a new order and set its details
        Order order = new Order();
        order.setBookId(bookId);
        order.setQuantity(quantity);
        order.setTotalPrice(book.getPrice() * quantity);
        order.setOrderDate(new Date());
        orderRepo.save(order);  // Save order in the database

        // Update the book quantity by making a PUT request to the LMS service
        QuantityRequest quantityRequest = new QuantityRequest(book.getQuantity() - quantity);
        restTemplate.put(lmsUri + bookId, quantityRequest);

        return "Order placed successfully for book ID: " + bookId;

    }
}
