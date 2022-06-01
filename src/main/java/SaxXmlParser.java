import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sax-Based xml parser.
 *
 * @author Sajad
 */
public class SaxXmlParser extends DefaultHandler implements ItemParser {
    private List<ItemModel> itemModels;

    ItemModel itemModel = new ItemModel();
    String str = "";

    @Override
    public void characters(char[] ch, int start, int length) {
        str = new String(ch, start, length).trim();
        System.out.println();
    }

    @Override
    public void startDocument() {
        itemModels = new ArrayList<>();
        itemModel = new ItemModel();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
        if ("SaleObject".equals(qName)) {
            itemModel = new ItemModel();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("SaleObject"))
            itemModels.add(itemModel);

        if (str.isEmpty()) return;

        if (qName.equalsIgnoreCase("sizeSqm"))
            itemModel.setSquareMeters(Integer.parseInt(str));
        else if (qName.equalsIgnoreCase("startingPrice"))
            itemModel.setStartingPrice(str);
        else if (qName.equalsIgnoreCase("city"))
            itemModel.setCity(str);
        else if (qName.equalsIgnoreCase("street"))
            itemModel.setStreet(str);
        else if (qName.equalsIgnoreCase("floor"))
            itemModel.setFloor(Integer.valueOf(str));
    }

    public List<ItemModel> getResult() {
        return itemModels;
    }

    @Override
    public List<ItemModel> getParsedRecords(String fileName) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try (InputStream is = getXMLFileAsStream(fileName)) {

            SAXParser saxParser = factory.newSAXParser();
            // parse XML and map to object, it works, but not recommend, try JAXB
            SaxXmlParser handler = new SaxXmlParser();
            saxParser.parse(is, handler);

            List<ItemModel> result = handler.getResult();

            return result;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            throw new SaleObjectConsumer.TechnicalException();
        }
    }

    private static InputStream getXMLFileAsStream(String fileName) {
        final var initialFile = new File("resources/" + fileName);

        InputStream targetStream = null;
        try {
            targetStream = new DataInputStream(new FileInputStream(initialFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return targetStream;
    }
}
