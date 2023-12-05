package com.uni.rmi.server.database;

import com.uni.common.controller.DatabaseController;
import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RemoteDatabaseController extends UnicastRemoteObject implements RemoteDatabaseInterface {
    private final DatabaseController databaseController;

    public RemoteDatabaseController(DatabaseController databaseController) throws RemoteException {
        super();
        this.databaseController = databaseController;
    }

    public void dropTables() throws RemoteException {
        databaseController.dropTables();
    }

    public void createAirlinesTable() throws RemoteException {
        databaseController.createAirlinesTable();
    }

    public void createFlightsTable() throws RemoteException {
        databaseController.createFlightsTable();
    }

    public void addAirline(Airline airline) throws RemoteException {
        databaseController.addAirline(airline);
    }

    public void addFlight(Flight flight) throws RemoteException {
        databaseController.addFlight(flight);
    }

    public Airline getAirlineById(String airlineId) throws RemoteException {
        return databaseController.getAirlineById(airlineId);
    }

    public Flight getFlightById(String flightId) throws RemoteException {
        return databaseController.getFlightById(flightId);
    }

    public List<Airline> getAllAirlines() throws RemoteException {
        return databaseController.getAllAirlines();
    }

    public List<Flight> getAllFlights() throws RemoteException {
        return databaseController.getAllFlights();
    }

    public void updateAirline(Airline airline) throws RemoteException {
        databaseController.updateAirline(airline);
    }

    public void updateFlight(Flight flight) throws RemoteException {
        databaseController.updateFlight(flight);
    }

    public void deleteAirlineById(String airlineId) throws RemoteException {
        databaseController.deleteAirlineById(airlineId);
    }

    public void deleteFlightById(String flightId) throws RemoteException {
        databaseController.deleteFlightById(flightId);
    }
}
