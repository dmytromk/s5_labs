package com.uni.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Airline implements Serializable {
    private final String id;
    private String name;
    private String country;

    public Airline(String id) {
        this.id = id;
    }

    public Airline(String id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Airline(String name, String country) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.country = country;
    }
}
