package com.uni.socket.server;

import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {
    private Socket socket;
    private Server server;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public RequestHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.reader = new ObjectInputStream(socket.getInputStream());
            this.writer = new ObjectOutputStream(socket.getOutputStream());
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
        List<Airline> authors = server.getDatabaseController().getAllAirlines();
        writer.writeObject(authors);
    }

    private void showFlights() throws IOException {
        List<Flight> flights = server.getDatabaseController().getAllFlights();
        writer.writeObject(flights);
    }
}
