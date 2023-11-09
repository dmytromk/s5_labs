package com.uni.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Flight {
    private final String id;
    private String name;
    private String origin;
    private String destination;
    private Double price;

    public Flight(String id, String name, String origin, String destination, Double price) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }
}
