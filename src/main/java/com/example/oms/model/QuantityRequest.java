package com.example.oms.model;

public class QuantityRequest {

    private int quantity;
    public QuantityRequest(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
