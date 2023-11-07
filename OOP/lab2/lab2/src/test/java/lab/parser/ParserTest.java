package lab.parser;

import lab.model.Characteristics;
import lab.model.Site;
import lab.model.Sites;
import lab.validator.XMLValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void test() {
        final double Eps = 0.00001;
        List<Sites> sitesList = new ArrayList<>();
        String filePathXML = "/Users/dmytromandziuk/uni/s5_labs/OOP/lab2/lab2/src/main/resources/internet-page.xml";
        String filePathXSD = "/Users/dmytromandziuk/uni/s5_labs/OOP/lab2/lab2/src/main/resources/internet-page.xsd";

        sitesList.add(MySAXParser.parse(filePathXML));
        sitesList.add(StAXParser.parse(filePathXML));
        sitesList.add(DOMParser.parse(filePathXML));

        assertTrue(XMLValidator.validate(filePathXML, filePathXSD));

        Site site1 = new Site("site1", "CNN", Site.SiteType.News, false);
        Characteristics characteristics1 = new Characteristics(true, true, false, true, Characteristics.VotingType.Anonymous);
        site1.setCharacteristics(characteristics1);

        Site site2 = new Site("site2", "Advertisement Pro", Site.SiteType.Advertisement, false);

        Site site3 = new Site("site3", "Example Portal Site", Site.SiteType.Portal, true);
        Characteristics characteristics3 = new Characteristics(true, false, true, true, Characteristics.VotingType.Authorized);
        site3.setCharacteristics(characteristics3);

        for (Sites sites : sitesList) {
            sites.sortById();
            assertEquals(sites.size(), 3);

            assertEquals(sites.get(0), site1);
            assertEquals(sites.get(1), site2);
            assertEquals(sites.get(2), site3);
        }
    }
}