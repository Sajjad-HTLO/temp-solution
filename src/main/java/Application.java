
import java.util.*;

public class Application {

    public static void main(String[] args) {
        SaleObjectConsumer saleObjectConsumer = null;

        if (!args[0].equals("-file")) throw new SaleObjectConsumer.TechnicalException();

        Set<String> fileNames = new HashSet<>(Arrays.asList(args[1].split(",")));

        // First validate the file names
        if (fileNames.stream().anyMatch(path -> !Helper.isValidFile(path)))
            throw new SaleObjectConsumer.TechnicalException();

        // Then parse the files
        for (String fileName : fileNames) {
            if (fileName.endsWith(".xml")) {
                ItemParser itemParser = new XmlItemParser();
                List<ItemModel> itemModels = itemParser.getParsedRecords(fileName);

                startTheFlow(itemModels, saleObjectConsumer);

                System.out.println();
            } else if (fileName.endsWith("json")) {
                ItemParser itemParser = new JsonItemParser();

                List<ItemModel> itemModels = itemParser.getParsedRecords(ItemParser.FILE_PREFIX.concat(fileName));

                startTheFlow(itemModels, saleObjectConsumer);

                System.out.println();
            }
        }
    }

    private static List<ItemModel> orderItems(List<ItemModel> itemModels, SaleObjectConsumer.PriorityOrderAttribute orderAttribute) {
        ItemComparator comparator = new ItemComparator(orderAttribute);
        Collections.sort(itemModels, comparator);

        return itemModels;
    }

    private static void startTheFlow(List<ItemModel> itemModels, SaleObjectConsumer objectConsumer) {
        List<ItemModel> orderedItems = orderItems(itemModels, objectConsumer.getPriorityOrderAttribute());

        // Second, call startSaleObjectTransaction()
        objectConsumer.startSaleObjectTransaction();

        // Third, for each item, call reportSaleObject()
        orderedItems.forEach(item -> objectConsumer.reportSaleObject(
                item.getSquareMeters(), String.valueOf(item.getPricePerSquareMeter()), item.getCity(), item.getStreet(), item.getFloor()));

        // Finally, call the commit
        objectConsumer.commitSaleObjectTransaction();
    }
}
