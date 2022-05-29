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
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Application {

    private final static String FILE_PREFIX = "resources/";

    public static void main(String[] args) {
        SaleObjectConsumer saleObjectConsumer = null;

        if (!args[0].equals("-file")) throw new SaleObjectConsumer.TechnicalException();

        Set<String> fileNames = new HashSet<>(Arrays.asList(args[1].split(",")));

        // First validate the file names
        if (fileNames.stream().anyMatch(path -> !isValidFile(path)))
            throw new SaleObjectConsumer.TechnicalException();

        // Then parse the files
        for (String fileName : fileNames) {
            if (fileName.endsWith(".xml")) {
                List<ItemModel> parsedObjects = getParsedRecords(fileName);

                SaleObjectConsumer.PriorityOrderAttribute orderAttribute = SaleObjectConsumer.PriorityOrderAttribute.PricePerSquareMeter;

                ItemComparator comparator = new ItemComparator(orderAttribute);
                Collections.sort(parsedObjects, comparator);

                System.out.println();
            }
        }

    }

    private static boolean isValidFile(String path) {
        try {
            return Files.exists(Paths.get(FILE_PREFIX + path));
            // Check the file is valid
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    private static List<ItemModel> getParsedRecords(String fileName) {
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

    private static int getPricePerSquareMeter(String textContent, int squareMeters) {
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

//        float sample = (float) 2075.4;
//         new BigDecimal("2075.4")
//                .multiply(new BigDecimal("1000"))
//                .intValue();

        // 10833.333 should be 10833333
    }
}
