package com.uni.common.controller;

import com.uni.common.model.Airline;
import com.uni.common.model.Flight;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CommonController {
    private final List<Airline> airlines;
    private final List<Flight> flights;

    public CommonController() {
        airlines = new ArrayList<>();
        flights = new ArrayList<>();
    }

    private Integer indexOfAirlineById(String id) {
        for (int i = 0; i < airlines.size(); i++) {
            if (airlines.get(i).getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    private Integer indexOfFlightById(String id) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    private List<Integer> sortedIndicesOfFlightsByAirlineId(String airlineId) {
        List<Integer> res = new ArrayList<>();
        for (int i = flights.size() - 1; i >= 0; i--) {
            if (flights.get(i).getAirlineId().equals(airlineId)) {
                res.add(i);
            }
        }
        return res;
    }

    public void clear() {
        airlines.clear();
        flights.clear();
    }

    public Airline getAirlineById(String id) {
        Integer i = indexOfAirlineById(id);
        if (i == null) {
            return null;
        }
        return airlines.get(i);
    }

    public Flight getFlightById(String id) {
        Integer i = indexOfFlightById(id);
        if (i == null) {
            return null;
        }
        return flights.get(i);
    }

    public void addAirline(Airline airline) {
        if (indexOfAirlineById(airline.getId()) != null) {
            throw new IllegalArgumentException();
        }
        airlines.add(airline);
    }

    public boolean deleteAirlineById(String id) {
        Integer i = indexOfAirlineById(id);
        if (i != null) {
            airlines.remove(i.intValue());
            List<Integer> indices = sortedIndicesOfFlightsByAirlineId(id);
            for (Integer index : indices) {
                flights.remove(index.intValue());
            }
            return true;
        }
        return false;
    }

    public void addFlight(Flight flight) {
        if (indexOfAirlineById(flight.getAirlineId()) == null) {
            throw new IllegalArgumentException();
        }
        if (indexOfFlightById(flight.getId()) != null) {
            throw new IllegalArgumentException();
        }
        flights.add(flight);
    }

    public boolean deleteFlightById(String id) {
        Integer i = indexOfFlightById(id);
        if (i != null) {
            flights.remove(i.intValue());
            return true;
        }
        return false;
    }

    public Integer countFlightsOfAirlineById(String id) {
        if (indexOfAirlineById(id) == null) {
            return null;
        }
        List<Integer> indices = sortedIndicesOfFlightsByAirlineId(id);
        return indices.size();
    }


    public List<Flight> getFlightsOfAirlineById(String id) {
        Integer i = indexOfAirlineById(id);
        if (i == null) {
            return null;
        }
        List<Flight> list = getSortedFlights().get(i);
        return new ArrayList<>(list);
    }

    public List<List<Flight>> getSortedFlights() {
        List<List<Flight>> res = new ArrayList<>();
        for (int i = 0; i < airlines.size(); i++) {
            res.add(new ArrayList<>());
        }
        for (Flight flight : flights) {
            Integer index = indexOfAirlineById(flight.getAirlineId());
            if (index == null) {
                throw new RuntimeException();
            }
            res.get(index).add(flight);
        }
        return res;
    }
}
