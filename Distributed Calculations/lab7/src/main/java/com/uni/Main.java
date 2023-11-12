package com.uni;

import com.uni.controller.CommonController;
import com.uni.controller.DatabaseController;
import com.uni.controller.XmlController;
import com.uni.model.Airline;
import com.uni.model.Flight;

import java.io.File;
import java.util.List;

public class Main {
    private final CommonController commonController;
    private final XmlController xmlController;
    private final DatabaseController databaseController;
    private final File xmlFile;
    private final File xsdFile;

    public Main(File xsdFile) {
        this.commonController = new CommonController();
        this.xmlController = new XmlController(commonController);
        this.databaseController = new DatabaseController();
        this.xmlFile = new File("/Users/dmytromandziuk/uni/s5_labs/Distributed Calculations/lab7/src/main/resources", "airline.xml");
        this.xsdFile = xsdFile;
    }

    public void addAirline(String name, String country) {
        commonController.addAirline(new Airline(name, country));
    }

    public void addAirline(String id, String name, String country) {
        commonController.addAirline(new Airline(id, name, country));
    }

    public void addFlight(String name, String origin, String destination, Double price, String airlineId) {
        commonController.addFlight(new Flight(name, origin, destination, price, airlineId));
    }

    public void fillCollection1() {
        addAirline("A1", "Airline1", "Country1");
        addFlight("Flight1_1", "Origin1", "Destination1", 100.0, "A1");
        addFlight("Flight1_2", "Origin2", "Destination2", 150.0, "A1");

        addAirline("A2", "Airline2", "Country2");
        addFlight("Flight2_1", "Origin3", "Destination3", 120.0, "A2");
        addFlight("Flight2_2", "Origin4", "Destination4", 180.0, "A2");

        addAirline("A3", "Airline1", "Country1");

        addAirline("A4", "Airline1", "Country1");
        addFlight("Flight4_1", "Origin5", "Destination5", 200.0, "A4");
        addFlight("Flight4_2", "Origin6", "Destination6", 250.0, "A4");
    }

    public void fillDatabase(CommonController controller) {
        databaseController.dropTables();
        databaseController.createAirlinesTable();
        databaseController.createFlightsTable();
        List<Airline> airlines = controller.getAirlines();
        List<Flight> flights = controller.getFlights();
        airlines.forEach(databaseController::addAirline);
        flights.forEach(databaseController::addFlight);
    }

    public void test1() {
        fillCollection1();
        System.out.println(xmlController.toXmlFormattedString(xsdFile));
        xmlController.toXmlFile(xmlFile, xsdFile);
    }

    public void test2() {
        fillCollection1();
        String str1 = xmlController.toXmlString(xsdFile);

        for (int i = 0; i < 100; i++) {
            xmlController.toXmlFile(xmlFile, xsdFile);
            commonController.clear();
            xmlController.fromXmlFile(xmlFile);
        }

        String str2 = xmlController.toXmlString(xsdFile);
        if (!str1.equals(str2)) {
            throw new RuntimeException();
        }
    }

    public void test3() {
        fillCollection1();
        fillDatabase(commonController);

        xmlController.toXmlFile(xmlFile, xsdFile);
        commonController.clear();
        xmlController.fromXmlFile(xmlFile);

        System.out.println("\n" + xmlController.toXmlFormattedString(xsdFile));

        Airline airline = new Airline("100", "Airline100", "Country100");
        Flight flight = new Flight("100", "Flight100", "Origin100", "Destination100", 500.0, "100");

        // ----------------------------------

        commonController.addAirline(airline);
        databaseController.addAirline(airline);

        commonController.deleteFlightById("1");
        databaseController.deleteFlightById("1");

        commonController.deleteAirlineById("2");
        databaseController.deleteAirlineById("2");

        commonController.addFlight(flight);
        databaseController.addFlight(flight);

        // ----------------------------------
        List<Airline> airlines = commonController.getAirlines();
        List<Flight> flights = commonController.getFlights();

        CommonController dbCollection = new CommonController();
        XmlController xmlController1 = new XmlController(dbCollection);
        airlines.forEach(dbCollection::addAirline);
        flights.forEach(dbCollection::addFlight);

        System.out.println("\n" + xmlController1.toXmlFormattedString(xsdFile));

        if (!xmlController1.toXmlString(xsdFile).equals(xmlController.toXmlString(xsdFile))) {
            System.out.println("\n" + xmlController.toXmlFormattedString(xsdFile));
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        new Main(new File("/Users/dmytromandziuk/uni/s5_labs/Distributed Calculations/lab7/src/main/resources", "airline.xsd")).test3();
    }

}
