package com.uni.rmi.client;

import com.uni.common.InputManager;
import com.uni.common.model.Airline;
import com.uni.common.model.Flight;
import com.uni.rmi.server.database.RemoteDatabaseInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class Client {
    private RemoteDatabaseInterface remoteDatabaseInterface;
    private final InputManager manager = new InputManager();

    public Client(){
        try{
            Registry reg = LocateRegistry.getRegistry(1234);
            this.remoteDatabaseInterface = (RemoteDatabaseInterface) reg.lookup("Airservice");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void printAirlines(List<Airline> airlines){
        if(airlines == null){
            return;
        }
        for(Airline airline : airlines){
            System.out.println(airline.toString());
        }
    }

    private void printFlight(List<Flight> flights){
        if(flights == null){
            return;
        }
        for(Flight flight : flights){
            System.out.println(flight.toString());
        }
    }

    private void printAvailableCommands() {
        System.out.println("Available Commands:");
        System.out.println("sa - Show Airlines");
        System.out.println("sf - Show Flights");
        System.out.println("ga - Get Airline");
        System.out.println("gf - Get Flight");
        System.out.println("aa - Add Airline");
        System.out.println("af - Add Flight");
        System.out.println("da - Delete Airlines");
        System.out.println("df - Delete Flights");
        System.out.println("ua - Update Airlines");
        System.out.println("uf - Update Flights");
        System.out.println("q, e, exit, quit - Quit the application");
    }

    private Airline createAirline() {
        System.out.println("\nYou are in an airline creation menu");
        Airline airline = new Airline();
        System.out.println("New airline`s ID is " + airline.getId());
        airline.setName(manager.getString("Enter name: "));
        airline.setCountry(manager.getString("Enter country: "));
        return airline;
    }

    private Flight createFlight(String airlineId) {
        System.out.println("\nYou are in a flight creation menu");
        Flight flight = new Flight();
        System.out.println("New flight`s ID is " + flight.getId());
        flight.setAirlineId(airlineId);
        flight.setName(manager.getString("Enter name : "));
        flight.setPrice(manager.getDouble("Enter price : "));
        flight.setOrigin(manager.getString("Enter origin : "));
        flight.setDestination(manager.getString("Enter destination : "));
        return flight;
    }

    private Airline modifyAirline(Airline airline) {
        System.out.println("\nYou are in an airline modification menu");
        System.out.println("Current state : \n" + airline);
        while (manager.getBoolean("Do you want change something? ")) {
            System.out.println(" n - name;\n c - country;");
            String input = manager.getString("Enter command : ");
            switch (input) {
                case "n" -> airline.setName(manager.getString("Enter name : "));
                case "c" -> airline.setCountry(manager.getString("Enter country : "));
                default -> System.out.println("Invalid command!");
            }
        }
        return airline;
    }

    private Flight modifyFlight(Flight flight) {
        System.out.println("\n You are in flight modification menu");
        System.out.println("Current state : \n" + flight);
        while (manager.getBoolean("Do you want change something? ")) {
            System.out.println(" n - name;\n p - price;\n o - origin;\n d - destination;");
            String input = manager.getString("Enter command : ");
            switch (input) {
                case "n" -> flight.setName(manager.getString("Enter name: "));
                case "p" -> flight.setPrice(manager.getDouble("Enter price: "));
                case "o" -> flight.setOrigin(manager.getString("Enter origin: "));
                case "d" -> flight.setDestination(manager.getString("Enter destination: "));
                default -> System.out.println("Invalid command!");
            }
        }
        return flight;
    }

    private void showAirlines() throws IOException {
        List<Airline> airlines = remoteDatabaseInterface.getAllAirlines();
        for (Airline airline: airlines) {
            System.out.println(airline.toString());
        }
    }

    private void showFlights() throws IOException {
        List<Flight> flights = remoteDatabaseInterface.getAllFlights();
        for (Flight flight: flights) {
            System.out.println(flight.toString());
        }
    }

    private void addAirline() throws IOException {
        Airline airline = createAirline();
        remoteDatabaseInterface.addAirline(airline);
    }

    private void addFlight() throws IOException {
        String airlineId = manager.getString("Enter airline ID: ");
        Airline airline = remoteDatabaseInterface.getAirlineById(airlineId);
        if (airline != null) {
            Flight flight = createFlight(airlineId);
            remoteDatabaseInterface.addFlight(flight);
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void getAirline() throws IOException {
        String airlineId = manager.getString("Enter airline ID: ");
        Airline airline = remoteDatabaseInterface.getAirlineById(airlineId);
        if (airline != null) {
            System.out.println(airline);
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void getFlight() throws IOException {
        String flightId = manager.getString("Enter flight ID: ");
        Airline flight = remoteDatabaseInterface.getAirlineById(flightId);
        if (flight != null) {
            System.out.println(flight);
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void updateAirline() throws IOException {
        String airlineId = manager.getString("Enter airline ID: ");
        Airline airline = remoteDatabaseInterface.getAirlineById(airlineId);
        if (airline != null) {
            remoteDatabaseInterface.updateAirline(modifyAirline(airline));
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void updateFlight() throws IOException {
        String flightId = manager.getString("Enter flight ID: ");
        Flight flight = remoteDatabaseInterface.getFlightById(flightId);
        if (flight != null) {
            remoteDatabaseInterface.updateFlight(modifyFlight(flight));
        } else {
            System.out.println("No flight with such an ID");
        }
    }

    private void deleteAirline() throws IOException {
        String airlineId = manager.getString("Enter airline ID: ");
        remoteDatabaseInterface.deleteAirlineById(airlineId);
    }

    private void deleteFlight() throws IOException {
        String flightId = manager.getString("Enter flight ID: ");
        remoteDatabaseInterface.deleteFlightById(flightId);
    }

    private void loop() throws IOException {
        while (true) {
            String input;
            input = manager.getString("Enter command : ");
            switch (input) {
                case "sa" -> showAirlines();
                case "sf" -> showFlights();
                case "aa" -> addAirline();
                case "af" -> addFlight();
                case "ga" -> getAirline();
                case "gf" -> getFlight();
                case "ua" -> updateAirline();
                case "uf" -> updateFlight();
                case "da" -> deleteAirline();
                case "df" -> deleteFlight();
                case "h" -> printAvailableCommands();
                case "q", "e", "exit", "quit" -> {
                    System.out.println("\nExiting...\n");
                    return;
                }
                default -> System.out.println("Invalid command!");
            }
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        try {
            client.printAvailableCommands();
            client.loop();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
