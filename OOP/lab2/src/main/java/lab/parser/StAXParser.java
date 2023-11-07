package lab.parser;

import lab.model.Characteristics;
import lab.model.Site;
import lab.model.Sites;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class StAXParser {
    public static Sites parse(String filename) {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(filename));
            return parseElements(reader);
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Sites parseElements(XMLStreamReader reader) throws XMLStreamException {
        Sites result = null;
        Sites currentSites = null;
        Site currentSite = null;
        Characteristics currentCharacteristics = null;
        String currentName = null;

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    currentName = reader.getLocalName();
                    switch (currentName) {
                        case "sites" -> currentSites = new Sites();
                        case "site" -> {
                            currentSite = new Site();
                            currentSite.setId(reader.getAttributeValue(null, "id"));
                        }
                        case "characteristics" -> currentCharacteristics = new Characteristics();
                    }
                }

                case XMLStreamConstants.CHARACTERS -> {
                    String value = reader.getText().trim();

                    if (value.isEmpty() || currentName == null)
                        continue;

                    if (currentSite == null) {
                        throw new XMLStreamException();
                    }

                    switch (currentName) {
                        case "title" -> currentSite.setTitle(value);
                        case "type" -> currentSite.setType(Site.SiteType.valueOf(value));
                        case "has_authorization" -> currentSite.setHasAuthorization(Boolean.parseBoolean(value));

                        default -> {
                            if (currentCharacteristics == null) {
                                throw new XMLStreamException();
                            }

                            switch (currentName) {
                                case "has_email" -> currentCharacteristics.setHasEmail(Boolean.parseBoolean(value));
                                case "has_news" -> currentCharacteristics.setHasNews(Boolean.parseBoolean(value));
                                case "has_archive" -> currentCharacteristics.setHasArchive(Boolean.parseBoolean(value));
                                case "voting" -> currentCharacteristics.setVotingType(Characteristics.VotingType.valueOf(value));
                                case "free" -> currentCharacteristics.setFree(Boolean.parseBoolean(value));
                            }
                        }
                    }
                }

                case XMLStreamConstants.END_ELEMENT -> {
                    String name = reader.getLocalName();

                    switch (name) {
                        case "sites" -> result = currentSites;
                        case "site" -> {
                            if (currentSites == null || currentSite == null) {
                                throw new XMLStreamException();
                            }
                            currentSites.add(currentSite);
                            currentSite = null;
                        }
                        case "characteristics" -> {
                            if (currentSite == null || currentCharacteristics == null) {
                                throw new XMLStreamException();
                            }
                            currentSite.setCharacteristics(currentCharacteristics);
                            currentCharacteristics = null;
                        }
                    }
                }
            }
        }

        return result;
    }
}
