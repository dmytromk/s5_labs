package com.uni.controller;

import com.uni.model.Airline;
import com.uni.model.Flight;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class XmlController {
    private final List<Airline> airlines;
    private final List<Flight> flights;

    public XmlController() {
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

//    public List<Airline> getAirlinesCopy() {
//        return new ArrayList<>(airlines);
//    }

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

    public String toXmlString(File xsdFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert doc != null;
        List<List<Flight>> sortedFlights = getSortedFlights();

        Element collectionElem = doc.createElement("collection");
        doc.appendChild(collectionElem);
        for (int i = 0; i < airlines.size(); i++) {
            Airline airline = airlines.get(i);
            Element airlineElem = doc.createElement("airline");
            airlineElem.setAttribute("id", airline.getId());
            airlineElem.setAttribute("name", airline.getName());
            airlineElem.setAttribute("country", airline.getCountry());
            collectionElem.appendChild(airlineElem);
            for (int j = 0; j < sortedFlights.get(i).size(); j++) {
                Flight flight = sortedFlights.get(i).get(j);
                Element flightElem = doc.createElement("flight");
                flightElem.setAttribute("id", flight.getId());
                flightElem.setAttribute("name", flight.getName());
                flightElem.setAttribute("origin", flight.getOrigin());
                flightElem.setAttribute("destination", flight.getDestination());
                flightElem.setAttribute("price", String.valueOf(flight.getPrice()));
                airlineElem.appendChild(flightElem);
            }
        }

        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Set the XSD schema location in the transformer
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }


    public String toXmlFormattedString(File xsdfFile) {
        StringWriter sw = new StringWriter();
        try {
            String str = toXmlString(xsdfFile);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source strSource = new StreamSource(new StringReader(str));
            Result strResult = new StreamResult(sw);
            transformer.transform(strSource, strResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public void toXmlFile(File xmlFile, File xsdfFile) {
        try {
            String str = toXmlString(xsdfFile);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source strSource = new StreamSource(new StringReader(str));
            Result fileResult = new StreamResult(xmlFile.getAbsoluteFile());
            transformer.transform(strSource, fileResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void fromXmlFile(File xmlFile) {
        clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {
                    System.out.println("Warning! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }

                @Override
                public void error(SAXParseException exception) {
                    System.out.println("Error! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }

                @Override
                public void fatalError(SAXParseException exception) {
                    System.out.println("Fatal error! " + exception.getMessage() + " Line: " + exception.getLineNumber());
                }
            });
            doc = db.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Element collectionElem = doc.getDocumentElement();
        if (!collectionElem.getTagName().equals("collection")) {
            throw new RuntimeException();
        }
        NodeList airlinesList = collectionElem.getElementsByTagName("airline");
        for (int i = 0; i < airlinesList.getLength(); i++) {
            Element airlineElem = (Element) airlinesList.item(i);
            String airlineId = airlineElem.getAttribute("id");
            String airlineName = airlineElem.getAttribute("name");
            String airlineCountry = airlineElem.getAttribute("country");
            Airline airline = new Airline(airlineId, airlineName, airlineCountry);
            addAirline(airline);
            NodeList flightsList = airlineElem.getElementsByTagName("flight");
            for (int j = 0; j < flightsList.getLength(); j++) {
                Element flightElem = (Element) flightsList.item(j);
                String flightId = flightElem.getAttribute("id");
                String flightName = flightElem.getAttribute("name");
                String flightOrigin = flightElem.getAttribute("origin");
                String flightDestination = flightElem.getAttribute("destination");
                Double flightPrice = Double.parseDouble(flightElem.getAttribute("price"));
                Flight flight = new Flight(flightId, flightName, flightOrigin, flightDestination, flightPrice, airlineId);
                addFlight(flight);
            }
        }
    }

}
