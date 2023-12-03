package com.uni.common.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
public class Flight implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String id;
    private String airlineId;
    private String name;
    private String origin;
    private String destination;
    private Double price;

    public Flight() {
        this.id = String.valueOf(UUID.randomUUID());
    }

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
