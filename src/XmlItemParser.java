import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XmlItemParser implements ItemParser {

    @Override
    public List<ItemModel> getParsedRecords(String fileName) {
        List<ItemModel> parsedItemModels = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FILE_PREFIX.concat(fileName)));

            Node sibling = doc.getDocumentElement().getFirstChild().getNextSibling();
            while (sibling != null) {

                if (sibling.getNodeType() == Node.ELEMENT_NODE) {

                    ItemModel itemModel = new ItemModel();

                    String id = sibling.getAttributes().getNamedItem("id").toString();
                    String type = sibling.getAttributes().getNamedItem("type").toString();

                    System.out.println("id: " + id);
                    System.out.println("type: " + type);

                    NodeList nodeList = sibling.getChildNodes();

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node current = nodeList.item(i);

                        if (current.getNodeType() == Node.ELEMENT_NODE) {
                            if (current.getNodeName().equalsIgnoreCase("sizeSqm"))
                                itemModel.setSquareMeters(Integer.parseInt(current.getTextContent()));
                            if (current.getNodeName().equalsIgnoreCase("startingPrice")) {
                                // price/sqm
                                itemModel.setPricePerSquareMeter(getPricePerSquareMeter(current.getTextContent(), itemModel.getSquareMeters()));
                            }

                            if (current.getNodeName().equalsIgnoreCase("address")) {
                                // Got further down
                                NodeList address = current.getChildNodes();
                                Node addressPart;
                                for (int j = 0; j < address.getLength(); j++) {
                                    addressPart = address.item(j);
                                    if (addressPart.getNodeType() == Node.ELEMENT_NODE) {
                                        if (addressPart.getNodeName().equalsIgnoreCase("city"))
                                            itemModel.setCity(addressPart.getTextContent());
                                        if (addressPart.getNodeName().equalsIgnoreCase("street"))
                                            itemModel.setStreet(addressPart.getTextContent());
                                        if (addressPart.getNodeName().equalsIgnoreCase("floor"))
                                            itemModel.setFloor(!addressPart.getTextContent().isEmpty() ? Integer.valueOf(addressPart.getTextContent()) : null);
                                    }
                                }

                            }
                        }
                    }

                    parsedItemModels.add(itemModel);
                }

                sibling = sibling.getNextSibling();
            }
        } catch (
                ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return parsedItemModels;
    }

    private int getPricePerSquareMeter(String textContent, int squareMeters) {
        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
        float value = 0;
        try {
            value = format.parse(textContent).floatValue() / squareMeters;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new BigDecimal(value)
                .multiply(new BigDecimal("1000"))
                .intValue();
    }
}
