package com.uni.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Flight {
    private final String id;
    private String airlineId;
    private String name;
    private String origin;
    private String destination;
    private Double price;

    public Flight(String id) {
        this.id = id;
    }

    public Flight(String name, String origin, String destination, Double price, String airlineId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.airlineId = airlineId;
    }

    public Flight(String id, String name, String origin, String destination, Double price, String airlineId) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.airlineId = airlineId;
    }
}
