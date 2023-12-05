package com.uni.socket.server;

import com.uni.common.JsonMapper;
import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;

    public RequestHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeClient() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAirlines() throws IOException {
        List<Airline> airlines;
        airlines = server.getDatabaseController().getAllAirlines();
        writer.println(JsonMapper.convertObjectToJson(airlines));
    }

    private void showFlights() throws IOException {
        List<Flight> flights;
        flights = server.getDatabaseController().getAllFlights();
        writer.println(JsonMapper.convertObjectToJson(flights));
    }

    private void getAirline() throws IOException {
        String id = reader.readLine();
        Airline airline = server.getDatabaseController().getAirlineById(id);
        if (airline != null) {
            writer.println(JsonMapper.convertObjectToJson(airline));
        } else {
            writer.println("[]");
        }
    }

    private void getFlight() throws IOException {
        String id = reader.readLine();
        Flight flight = server.getDatabaseController().getFlightById(id);
        if (flight != null) {
            writer.println(JsonMapper.convertObjectToJson(flight));
        } else {
            writer.println("[]");
        }
    }

    private void addAirline() throws IOException {
        String value = reader.readLine();
        Airline airline = JsonMapper.convertJsonToObject(value, Airline.class);
        server.getDatabaseController().addAirline(airline);
    }

    private void addFlight() throws IOException {
        String value = reader.readLine();
        Flight flight = JsonMapper.convertJsonToObject(value, Flight.class);
        server.getDatabaseController().addFlight(flight);
    }

    private void deleteAirline() throws IOException {
        String id = reader.readLine();
        server.getDatabaseController().deleteAirlineById(id);
    }

    private void deleteFlight() throws IOException {
        String id = reader.readLine();
        server.getDatabaseController().deleteFlightById(id);
    }

    private void updateAirline() throws IOException {
        String value = reader.readLine();
        Airline airline = JsonMapper.convertJsonToObject(value, Airline.class);
        server.getDatabaseController().updateAirline(airline);
    }

    private void updateFlight() throws IOException {
        String value = reader.readLine();
        Flight flight = JsonMapper.convertJsonToObject(value, Flight.class);
        server.getDatabaseController().updateFlight(flight);
    }

    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                String input = reader.readLine();
                writer.flush();
                switch (input) {
                    case "sa":
                        showAirlines();
                        break;
                    case "sf":
                        showFlights();
                        break;
                    case "ga":
                        getAirline();
                        break;
                    case "gf":
                        getFlight();
                        break;
                    case "aa":
                        addAirline();
                        break;
                    case "af":
                        addFlight();
                        break;
                    case "da":
                        deleteAirline();
                        break;
                    case "df":
                        deleteFlight();
                        break;
                    case "ua":
                        updateAirline();
                        break;
                    case "uf":
                        updateFlight();
                        break;
                    case "q", "e", "exit", "quit":
                        closeClient();
                        return;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeClient();
        }
    }
}
