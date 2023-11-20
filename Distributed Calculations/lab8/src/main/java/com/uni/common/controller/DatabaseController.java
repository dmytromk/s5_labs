package com.uni.common.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

import com.uni.common.model.Airline;
import com.uni.common.model.Flight;

public class DatabaseController {
    private Connection currentConnection = null;
    private final ConnectionPool connectionPool;

    public DatabaseController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    public void dropTables() {
        String sql1 = "DROP TABLE IF EXISTS airlines";
        String sql2 = "DROP TABLE IF EXISTS flights";

        try {
            this.currentConnection = connectionPool.getConnection();
            Statement st = currentConnection.createStatement();
            st.execute(sql1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            this.currentConnection = connectionPool.getConnection();
            Statement st = currentConnection.createStatement();
            st.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createAirlinesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS airlines (\n"
                + "     id text PRIMARY KEY, \n"
                + "     name text NOT NULL, \n"
                + "     country text NOT NULL \n"
                + ");";

        try {
            this.currentConnection = connectionPool.getConnection();
            Statement st = currentConnection.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFlightsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS flights (\n"
                + "     id text PRIMARY KEY, \n"
                + "     name text NOT NULL, \n"
                + "     origin text NOT NULL, \n"
                + "     destination text NOT NULL, \n"
                + "     price real NOT NULL, \n"
                + "     airline_id text NOT NULL \n"
                + ");";

        try {
            this.currentConnection = connectionPool.getConnection();
            Statement st = currentConnection.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAirline(Airline airline) {
        String sql = "INSERT INTO airlines (id, name, country) VALUES (?, ?, ?)";
        
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, airline.getId());
            pstmt.setString(2, airline.getName());
            pstmt.setString(3, airline.getCountry());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting airline: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addFlight(Flight flight) {
        String sql = "INSERT INTO flights (id, name, origin, destination, price, airline_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, flight.getId());
            pstmt.setString(2, flight.getName());
            pstmt.setString(3, flight.getOrigin());
            pstmt.setString(4, flight.getDestination());
            pstmt.setDouble(5, flight.getPrice());
            pstmt.setString(6, flight.getAirlineId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting flight: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Airline getAirlineById(String airlineId) {
        String sql = "SELECT * FROM airlines WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, airlineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String country = rs.getString("country");
                    return new Airline(id, name, country);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving airline by ID: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Flight getFlightById(String flightId) {
        String sql = "SELECT * FROM flights WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, flightId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String origin = rs.getString("origin");
                    String destination = rs.getString("destination");
                    double price = rs.getDouble("price");
                    String airlineId = rs.getString("airline_id");
                    return new Flight(id, name, origin, destination, price, airlineId);
                }
            } finally {
                try {
                    this.connectionPool.releaseConnection(this.currentConnection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving flight by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Airline> getAllAirlines() {
        List<Airline> airlines = new ArrayList<>();
        String sql = "SELECT * FROM airlines";
        try {
            this.currentConnection = connectionPool.getConnection();
            Statement stmt = currentConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String country = rs.getString("country");
                Airline airline = new Airline(id, name, country);
                airlines.add(airline);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving airlines: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return airlines;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try {
            this.currentConnection = connectionPool.getConnection();
            Statement stmt = currentConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");
                double price = rs.getDouble("price");
                String airlineId = rs.getString("airline_id");
                Flight flight = new Flight(id, name, origin, destination, price, airlineId);
                flights.add(flight);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving flights: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flights;
    }

    public void updateAirline(Airline airline) {
        String sql = "UPDATE airlines SET name = ?, country = ? WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, airline.getName());
            pstmt.setString(2, airline.getCountry());
            pstmt.setString(3, airline.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating airline: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFlight(Flight flight) {
        String sql = "UPDATE flights SET name = ?, origin = ?, destination = ?, price = ?, airline_id = ? WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, flight.getName());
            pstmt.setString(2, flight.getOrigin());
            pstmt.setString(3, flight.getDestination());
            pstmt.setDouble(4, flight.getPrice());
            pstmt.setString(5, flight.getAirlineId());
            pstmt.setString(6, flight.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating flight: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAirlineById(String airlineId) {
        String sql = "DELETE FROM airlines WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, airlineId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting airline: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFlightById(String flightId) {
        String sql = "DELETE FROM flights WHERE id = ?";
        try {
            this.currentConnection = connectionPool.getConnection();
            PreparedStatement pstmt = currentConnection.prepareStatement(sql);
            pstmt.setString(1, flightId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting flight: " + e.getMessage());
        } finally {
            try {
                this.connectionPool.releaseConnection(this.currentConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
