
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
                List<ItemModel> parsedObjects = itemParser.getParsedRecords(fileName);

                SaleObjectConsumer.PriorityOrderAttribute orderAttribute = SaleObjectConsumer.PriorityOrderAttribute.PricePerSquareMeter;

                ItemComparator comparator = new ItemComparator(orderAttribute);
                Collections.sort(parsedObjects, comparator);

                System.out.println();
            } else if (fileName.endsWith("json")) {
                ItemParser itemParser = new JsonItemParser();

                List<ItemModel> itemModels = itemParser.getParsedRecords(ItemParser.FILE_PREFIX.concat(fileName));

                System.out.println();
            }
        }
    }
}
