package com.uni.controller;

import com.uni.model.Airline;
import com.uni.model.Flight;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XmlController {
    private CommonController commonController;

    public XmlController(CommonController commonController) {
        this.commonController = commonController;
    }

    private String documentToString(Document doc) {
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private boolean validateXmlAgainstXsd(String xml, File xsdFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

        List<List<Flight>> sortedFlights = commonController.getSortedFlights();
        List<Airline> airlines = commonController.getAirlines();

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

        String xmlString = documentToString(doc);

        if (validateXmlAgainstXsd(xmlString, xsdFile)) {
            return xmlString;
        } else {
            return null;
        }
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
        commonController.clear();
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
            commonController.addAirline(airline);
            NodeList flightsList = airlineElem.getElementsByTagName("flight");
            for (int j = 0; j < flightsList.getLength(); j++) {
                Element flightElem = (Element) flightsList.item(j);
                String flightId = flightElem.getAttribute("id");
                String flightName = flightElem.getAttribute("name");
                String flightOrigin = flightElem.getAttribute("origin");
                String flightDestination = flightElem.getAttribute("destination");
                Double flightPrice = Double.parseDouble(flightElem.getAttribute("price"));
                Flight flight = new Flight(flightId, flightName, flightOrigin, flightDestination, flightPrice, airlineId);
                commonController.addFlight(flight);
            }
        }
    }

}
