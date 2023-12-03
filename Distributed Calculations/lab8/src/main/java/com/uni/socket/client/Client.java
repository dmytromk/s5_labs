package com.uni.socket.client;

import com.uni.common.InputManager;
import com.uni.common.JsonMapper;
import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client {
    private InputManager manager = new InputManager();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean exit = false;

    public Client(String host, int portId) {
        try {
            clientSocket = new Socket(host, portId);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeClient() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
        System.out.println("\nYou are in an airline creation menu\n");
        Airline airline = new Airline();
        System.out.println("\nNew airline`s ID is " + airline.getId());
        airline.setName(manager.getString("Enter name: "));
        airline.setCountry(manager.getString("Enter country: "));
        return airline;
    }

    private Flight createFlight() {
        System.out.println("\n You are in a flight creation menu\n");
        Flight flight = new Flight();
        System.out.println("\n New flight`s ID is " + flight.getId());
        flight.setAirlineId(manager.getString("Enter airline's ID : "));
        flight.setName(manager.getString("Enter name : "));
        flight.setPrice(manager.getDouble("Enter price : "));
        flight.setOrigin(manager.getString("Enter origin : "));
        flight.setDestination(manager.getString("Enter destination : "));
        return flight;
    }

    private Airline modifyAirline(Airline airline) {
        System.out.println("\n You are in an airline modification menu\n");
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
        System.out.println("\n You are in flight modification menu\n");
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
        out.println("sa");
        List<Airline> airlines = JsonMapper.convertJsonToList(in.readLine(), Airline.class);
        if (airlines == null) {
            return;
        }
        for (Airline airline : airlines) {
            System.out.println(airline.toString());
        }
    }

    private void showFlights() throws IOException {
        out.println("sf");
        List<Flight> flights = JsonMapper.convertJsonToList(in.readLine(), Flight.class);
        if (flights == null) {
            return;
        }
        for (Flight flight : flights) {
            System.out.println(flight.toString());
        }
    }

    private void addAirline() throws IOException {
        out.println("aa");
        Airline airline = createAirline();
        out.println(JsonMapper.convertObjectToJson(airline));
    }

    private void addFlight() throws IOException {
        out.println("ga");
        String airlineId = manager.getString("Enter airline ID: ");
        out.println(airlineId);
        Airline airline = JsonMapper.convertJsonToObject(in.readLine(), Airline.class);
        if (airline == null) {
            out.println("af");
            Flight flight = createFlight();
            out.println(JsonMapper.convertObjectToJson(flight));
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void getAirline() throws IOException {
        out.println("ga");
        String airlineId = manager.getString("Enter airline ID: ");
        out.println(airlineId);
        Airline airline = JsonMapper.convertJsonToObject(in.readLine(), Airline.class);
        if (airline != null) {
            System.out.println(airline);
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void getFlight() throws IOException {
        out.println("gf");
        String flightId = manager.getString("Enter flight ID: ");
        out.println(flightId);
        Flight flight = JsonMapper.convertJsonToObject(in.readLine(), Flight.class);
        if (flight != null) {
            System.out.println(flight);
        } else {
            System.out.println("No flight with such an ID");
        }
    }

    private void updateAirline() throws IOException {
        out.println("ga");
        String airlineId = manager.getString("Enter airline ID: ");
        out.println(airlineId);
        Airline airline = JsonMapper.convertJsonToObject(in.readLine(), Airline.class);
        if (airline != null) {
            out.println("ua");
            out.println(JsonMapper.convertObjectToJson(modifyAirline(airline)));
        } else {
            System.out.println("No airline with such an ID");
        }
    }

    private void updateFlight() throws IOException {
        out.println("gf");
        String flightId = manager.getString("Enter flight ID: ");
        out.println(flightId);
        Flight flight = JsonMapper.convertJsonToObject(in.readLine(), Flight.class);
        if (flight != null) {
            out.println("uf");
            out.println(JsonMapper.convertObjectToJson(modifyFlight(flight)));
        } else {
            System.out.println("No flight with such an ID");
        }
    }

    private void deleteAirline() throws IOException {
        out.println("da");
        String airlineId = manager.getString("Enter airline ID: ");
        out.println(airlineId);
    }

    private void deleteFlight() throws IOException {
        out.println("db");
        String flightId = manager.getString("Enter flight ID: ");
        out.println(flightId);
    }

    private void loop() throws IOException {
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
                out.println("e");
                exit = true;
                return;
            }
            default -> System.out.println("Invalid command!");
        }
    }
}
