package lab.parser;

import lab.model.Characteristics;
import lab.model.Site;
import lab.model.Sites;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DOMParser {

    public static Sites parse(String filename) {
        try {
            DocumentBuilder factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = factory.parse(new File(filename));
            document.getDocumentElement().normalize();
            return processDocument(document);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Sites processDocument(Document document) {
        NodeList siteList = document.getElementsByTagName("site");
        Sites sites = new Sites();

        for (int i = 0; i < siteList.getLength(); i++) {
            Node siteNode = siteList.item(i);
            Site site = new Site();
            Characteristics characteristics = new Characteristics();

            if (siteNode.getNodeType() == Node.ELEMENT_NODE) {
                Element siteElement = (Element) siteNode;
                site.setId(siteElement.getAttribute("id"));
                site.setTitle(siteElement.getElementsByTagName("title").item(0).getTextContent());
                site.setHasAuthorization(Boolean.parseBoolean(siteElement.getElementsByTagName("has_authorization").item(0).getTextContent()));
                site.setType(Site.SiteType.valueOf(siteElement.getElementsByTagName("type").item(0).getTextContent()));

                Element characteristicsElement = (Element) siteElement.getElementsByTagName("characteristics").item(0);
                if(characteristicsElement != null) {
                    characteristics.setFree(Boolean.parseBoolean(characteristicsElement.getElementsByTagName("free").item(0).getTextContent()));
                    characteristics.setHasArchive(Boolean.parseBoolean(characteristicsElement.getElementsByTagName("has_archive").item(0).getTextContent()));
                    characteristics.setHasEmail(Boolean.parseBoolean(characteristicsElement.getElementsByTagName("has_email").item(0).getTextContent()));
                    characteristics.setHasNews(Boolean.parseBoolean(characteristicsElement.getElementsByTagName("has_news").item(0).getTextContent()));
                    characteristics.setVotingType(Characteristics.VotingType.valueOf(characteristicsElement.getElementsByTagName("voting").item(0).getTextContent()));
                    site.setCharacteristics(characteristics);
                }
            }

            sites.add(site);
        }
        return sites;
    }
}
