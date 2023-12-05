package com.uni.rmi.server.database;

import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteDatabaseInterface extends Remote{
    void dropTables() throws RemoteException;

    void createAirlinesTable() throws RemoteException;

    void createFlightsTable() throws RemoteException;

    void addAirline(Airline airline) throws RemoteException;

    void addFlight(Flight flight) throws RemoteException;

    Airline getAirlineById(String airlineId) throws RemoteException;

    Flight getFlightById(String flightId) throws RemoteException;

    List<Airline> getAllAirlines() throws RemoteException;

    List<Flight> getAllFlights() throws RemoteException;

    void updateAirline(Airline airline) throws RemoteException;

    void updateFlight(Flight flight) throws RemoteException;

    void deleteAirlineById(String airlineId) throws RemoteException;

    void deleteFlightById(String flightId) throws RemoteException;
}
