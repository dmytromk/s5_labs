package com.uni.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Airline {
    private final String id;
    private String name;
    private String country;
    private List<Flight> flights;

    public Airline(String id) {
        this.id = id;
    }

    public Airline(String id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.flights = new ArrayList<>();
    }

    public Airline(String name, String country) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.country = country;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public void removeFlight(String flightId){
        for(Flight flight : flights){
            if(flight.getId().equals(flightId)){
                flights.remove(flight);
                return;
            }
        }
    }
}
