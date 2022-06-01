//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.XMLConstants;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Dom-Based xml parser
// */
//public class XmlItemParser implements ItemParser {
//
//    @Override
//    public List<ItemModel> getParsedRecords(String fileName) {
//        List<ItemModel> parsedItemModels = new ArrayList<>();
//
//        try {
//            Document doc = getDocumentInstance(fileName);
//            Element docElement = doc.getDocumentElement();
//
//            int count = Integer.parseInt(docElement.getAttribute("count"));
//
//            if (count > 2) {
//                NodeList childNodes = docElement.getChildNodes();
//
//                for (int i = 0; i < childNodes.getLength(); i++) {
//                    Node each = childNodes.item(i);
//
//                    if (each.getNodeType() == Node.ELEMENT_NODE) {
//                        ItemModel itemModel = new ItemModel();
//
//                        itemModel.setType(Helper.resolveType(each.getAttributes().getNamedItem("type").getTextContent()));
//
//                        NodeList nodeList = each.getChildNodes();
//
//                        for (int j = 0; j < nodeList.getLength(); j++) {
//                            Node current = nodeList.item(j);
//
//                            if (current.getNodeType() == Node.ELEMENT_NODE) {
//                                if (current.getNodeName().equalsIgnoreCase("sizeSqm"))
//                                    itemModel.setSquareMeters(Integer.parseInt(current.getTextContent()));
//                                if (current.getNodeName().equalsIgnoreCase("startingPrice")) {
//                                    // price/sqm
//                                    itemModel.setPricePerSquareMeter(Helper.getPricePerSquareMeter(current.getTextContent(), itemModel.getSquareMeters()));
//                                }
//
//                                // Go further down to resolve address
//                                resolveAddress(current, itemModel);
//                            }
//                        }
//
//                        // Add each object to the collection
//                        parsedItemModels.add(itemModel);
//                    }
//                }
//            }
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }
//
//        return parsedItemModels;
//    }
//
//    private Document getDocumentInstance(String fileName) throws ParserConfigurationException, IOException, SAXException {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//
//        // parse XML file
//        DocumentBuilder db = dbf.newDocumentBuilder();
//
//        // Create a document instance
//        return db.parse(new File(FILE_PREFIX.concat(fileName)));
//    }
//
//    private void resolveAddress(Node current, ItemModel itemModel) {
//        if (current.getNodeName().equalsIgnoreCase("address")) {
//            NodeList address = current.getChildNodes();
//            Node addressPart;
//            for (var j = 0; j < address.getLength(); j++) {
//                addressPart = address.item(j);
//                if (addressPart.getNodeType() == Node.ELEMENT_NODE) {
//                    if (addressPart.getNodeName().equalsIgnoreCase("city"))
//                        itemModel.setCity(addressPart.getTextContent());
//                    if (addressPart.getNodeName().equalsIgnoreCase("street"))
//                        itemModel.setStreet(addressPart.getTextContent());
//                    if (addressPart.getNodeName().equalsIgnoreCase("floor"))
//                        itemModel.setFloor(!addressPart.getTextContent().isEmpty() ? Integer.valueOf(addressPart.getTextContent()) : null);
//                }
//            }
//        }
//    }
//}
