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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
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

//    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final String lmsBaseUri = "http://localhost:8080/api/books";

    // Create a parameterized type reference to handle the response
    ParameterizedTypeReference<List<Book>> typeRef = new ParameterizedTypeReference<>() {};

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    public List<Book> getBooksByCategory(String category){
        ResponseEntity<List<Book>> booksByCategory = restTemplate.exchange(
                lmsBaseUri+"?category="+category,
                HttpMethod.GET,
                null,
                typeRef
        );
        List<Book> booksFound = booksByCategory.getBody();
//        logger.info("{}",booksFound);
        return booksFound;
    }
    public List<Book> getBooksByAuthorName(String authorName){
        ResponseEntity<List<Book>> booksByAuthorName = restTemplate.exchange(
                lmsBaseUri+"?authorName="+authorName,
                HttpMethod.GET,
                null,
                typeRef
        );
        List<Book> booksFound = booksByAuthorName.getBody();
//        logger.info("{}",booksFound);
        return booksFound;
    }

    public String placeOrderByCategory(String category, int bookId, int quantity) {
        try {
            // Make the GET request and retrieve the response entity
            List<Book> categoryBooks = getBooksByCategory(category);

            // Get books by ID
            ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                    lmsBaseUri + "?bookId="+bookId + "&category=" + category,
                    HttpMethod.GET,
                    null,
                    typeRef
            );

            // Retrieve the list of books from the response
            List<Book> books = responseEntity.getBody();

            // Check if the books list is null or empty
            if (books == null || books.isEmpty()) {
                return "Book not found with ID: " + bookId;
            }

            // Get the first book object
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
            restTemplate.put(lmsBaseUri+"?bookId="+bookId, quantityRequest);

            return "Order placed successfully for book ID: " + bookId;

        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 error specifically
            return "Book not found with ID: " + bookId + " in category: " + category;
        } catch (RestClientException e) {
            // Handle other errors like 500, bad request, etc.
//            logger.error("Error while placing order: {}", e.getMessage());
            return "An error occurred while placing the order. Please try again later.";
        }
    }

    public String placeOrderByAuthorName(String authorName, int bookId, int quantity) {
        try {

            // Get books by ID and authorName
            ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                    lmsBaseUri + "?bookId="+bookId + "&authorName=" + authorName,
                    HttpMethod.GET,
                    null,
                    typeRef
            );

            // Retrieve the list of books from the response
            List<Book> books = responseEntity.getBody();

            // Check if the books list is null or empty
            if (books == null || books.isEmpty()) {
                return "Book not found with ID: " + bookId;
            }

            // Get the first book object
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
            restTemplate.put(lmsBaseUri+"?bookId="+bookId, quantityRequest);

            return "Order placed successfully for book ID: " + bookId;

        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 error specifically
            return "Book not found with ID: " + bookId + " with Author: " + authorName;
        } catch (RestClientException e) {
            // Handle other errors like 500, bad request, etc.
//            logger.error("Error while placing order: {}", e.getMessage());
            return "An error occurred while placing the order. Please try again later.";
        }
    }



}
