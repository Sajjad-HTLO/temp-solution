import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
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
    StringBuilder str = new StringBuilder();
    private ItemModel item;

    /**
     * Returns stream of the file.
     *
     * @param fileName File name.
     * @return {@linkplain InputStream} of the file for the given fileme.
     */
    private static InputStream getXMLFileAsStream(String fileName) {
        final var initialFile = new File(FILE_PREFIX + fileName);

        InputStream targetStream = null;
        try {
            targetStream = new DataInputStream(new FileInputStream(initialFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return targetStream;
    }

    @Override
    public void startDocument() {
        itemModels = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        str = new StringBuilder();
        str.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
        if ("SaleObject".equals(qName)) {
            item = new ItemModel();
            item.setId(Integer.parseInt(attr.getValue("id")));
            item.setType(Helper.resolveType(attr.getValue("type").toUpperCase()));
            System.out.println();
        }
    }

    public List<ItemModel> getResult() {
        return itemModels;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("SaleObject")) {
            // Set price per square meter and add to the collection
            item.setPricePerSquareMeter(Helper.getPricePerSquareMeter(item.getStartingPrice(), item.getSquareMeters()));
            itemModels.add(item);
        }

        // If this is an empty tag, skip it
        if (str.toString().trim().isEmpty()) return;

        setCommonAttributes(qName, item);
    }

    @Override
    public List<ItemModel> getParsedRecords(String fileName) {
        var factory = SAXParserFactory.newInstance();
        try (InputStream is = getXMLFileAsStream(fileName)) {

            var saxParser = factory.newSAXParser();
            // parse XML and map to object, it works, but not recommend, try JAXB
            var handler = new SaxXmlParser();
            saxParser.parse(is, handler);

            List<ItemModel> result = handler.getResult();

            return result;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            throw new SaleObjectConsumer.TechnicalException();
        }
    }

    private void setCommonAttributes(String name, ItemModel item) {
        if (name.equalsIgnoreCase("sizeSqm"))
            item.setSquareMeters(Integer.parseInt(str.toString()));
        else if (name.equalsIgnoreCase("startingPrice"))
            item.setStartingPrice(str.toString());
        else if (name.equalsIgnoreCase("city"))
            item.setCity(str.toString());
        else if (name.equalsIgnoreCase("street"))
            item.setStreet(str.toString());
        else if (name.equalsIgnoreCase("floor"))
            item.setFloor(Integer.valueOf(str.toString()));
    }
}
