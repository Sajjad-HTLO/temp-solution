import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
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
                ItemParser itemParser = new XmlItemParser();
                List<ItemModel> parsedObjects = itemParser.getParsedRecords(fileName);

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
}
