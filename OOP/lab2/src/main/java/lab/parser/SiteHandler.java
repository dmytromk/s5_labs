package lab.parser;

import lab.model.Characteristics;
import lab.model.Site;
import lab.model.Sites;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SiteHandler extends DefaultHandler {
    @Getter
    private Sites result = null;
    private Sites currentSites = null;
    private Site currentSite = null;
    private Characteristics currentCharacteristics = null;
    private StringBuilder currentValue = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "sites" -> currentSites = new Sites();
            case "site" -> {
                currentSite = new Site();
                currentSite.setId(attributes.getValue("id"));
            }
            case "characteristics" -> currentCharacteristics = new Characteristics();
            default -> currentValue = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentValue == null) {
            currentValue = new StringBuilder();
        } else {
            currentValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String value = currentValue.toString().trim();
        
        switch (qName) {
            case "sites" -> {
                result = currentSites;
            }
            case "site" -> {
                if (currentSites == null) currentSites = new Sites();
                currentSites.add(currentSite);
                currentSite = null;
            }
            case "characteristics" -> {
                currentSite.setCharacteristics(currentCharacteristics);
                currentCharacteristics = null;
            }

            case "title" -> currentSite.setTitle(value);
            case "type" -> currentSite.setType(Site.SiteType.valueOf(value));
            case "has_authorization" -> currentSite.setHasAuthorization(Boolean.parseBoolean(value));

            case "has_email" -> currentCharacteristics.setHasEmail(Boolean.parseBoolean(value));
            case "has_news" -> currentCharacteristics.setHasNews(Boolean.parseBoolean(value));
            case "has_archive" -> currentCharacteristics.setHasArchive(Boolean.parseBoolean(value));
            case "voting" -> currentCharacteristics.setVotingType(Characteristics.VotingType.valueOf(value));
            case "free" -> currentCharacteristics.setFree(Boolean.parseBoolean(value));
        }
    }

}