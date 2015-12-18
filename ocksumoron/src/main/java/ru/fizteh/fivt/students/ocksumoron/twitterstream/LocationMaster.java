package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import twitter4j.GeoLocation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class LocationMaster {

    private static final int DEFAULT_RES = 5;

    private static GeoLocation getCoordinates(NodeList sections) {
        Element section = (Element) sections.item(0);
        String coordinates = section.getTextContent();
        String[] coordinatesParsed = coordinates.split(" ");
        Double longitude = Double.parseDouble(coordinatesParsed[0]);
        Double latitude = Double.parseDouble(coordinatesParsed[1]);
        return new GeoLocation(latitude, longitude);
    }

    private static Document documentResolver(URL url)
            throws IOException, ParserConfigurationException, SAXException {
        URLConnection geoConnection = url.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        factory.setNamespaceAware(false);
        factory.setValidating(false);

        DocumentBuilder parser = factory.newDocumentBuilder();

        Document document = parser.parse(geoConnection.getInputStream());
        return document;
    }

    public static Location getLocation(String place) throws InvalidLocationException {
        try {
            if (place.equals("nearby")) {
                Document document = documentResolver(new URL("http://api.hostip.info/"));
                NodeList requiredTagList = document.getElementsByTagName("gml:name");
                if (requiredTagList.getLength() < 2) {
                    return new Location(-1);
                }
                Element section = (Element) requiredTagList.item(1);
                place = section.getTextContent();
            }

            Document document = documentResolver(new URL("https://geocode-maps.yandex.ru/1.x/?geocode=" + place));
            if (document.getElementsByTagName("pos").getLength() == 0) {
                throw new InvalidLocationException();
            }
            GeoLocation centerPos = getCoordinates(document.getElementsByTagName("pos"));
            GeoLocation swPos = getCoordinates(document.getElementsByTagName("lowerCorner"));
            GeoLocation nePos = getCoordinates(document.getElementsByTagName("upperCorner"));
            double res = DEFAULT_RES;
            return new Location(centerPos, swPos, nePos, res);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        throw new InvalidLocationException();
    }
}
