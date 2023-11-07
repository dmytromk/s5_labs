package lab.parser;

import lab.model.Sites;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class MySAXParser {
    public static Sites parse(String filePath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            SiteHandler handler = new SiteHandler();
            parser.parse(filePath, handler);
            return handler.getResult();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
