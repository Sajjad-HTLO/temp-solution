
import java.util.*;

/**
 * Main application is responsible to validate arguments, parse the data, and start the main flow.
 *
 * @author Sajad
 */
public class Application {

    /**
     * Main entry point of the application to validate, parse the arguments and finally call the main flow.
     *
     * @param args Given line arguments.
     */
    public static void main(String[] args) {
        SaleObjectConsumer saleObjectConsumer = null;

        if (!args[0].equals("-file")) throw new SaleObjectConsumer.TechnicalException();

        Set<String> fileNames = new HashSet<>(Arrays.asList(args[1].split(",")));

        // First validate the file names
        if (fileNames.stream().anyMatch(path -> !Helper.isValidFile(path)))
            throw new SaleObjectConsumer.TechnicalException();

        // Then parse the files
        for (String fileName : fileNames) {
            if (Helper.isXmlFile(fileName)) {
                ItemParser parser = new SaxXmlParser();

                List<ItemModel> itemModels = parser.getParsedRecords(fileName);

                System.out.println();

//                startTheFlow(itemModels, saleObjectConsumer);
            } else if (Helper.isJsonFile(fileName)) {
                ItemParser itemParser = new JsonItemParser();

                List<ItemModel> itemModels = itemParser.getParsedRecords(ItemParser.FILE_PREFIX.concat(fileName));

//                startTheFlow(itemModels, saleObjectConsumer);

                System.out.println();
            }
        }
    }

    /**
     * Order given {@code itemModels} based on the {@code orderAttribute}
     *
     * @param itemModels     Collection of {@linkplain ItemModel} objects.
     * @param orderAttribute Given order attribute to order upon it.
     * @return Ordered version of given {@code itemModels}.
     */
    private static List<ItemModel> orderItems(List<ItemModel> itemModels, SaleObjectConsumer.PriorityOrderAttribute orderAttribute) {
        ItemComparator comparator = new ItemComparator(orderAttribute);
        Collections.sort(itemModels, comparator);

        return itemModels;
    }

    /**
     * Start the main flow by taking the required steps sequentially.
     *
     * @param itemModels     Collection of {@linkplain ItemModel} objects.
     * @param objectConsumer Given consumer instance.
     */
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
