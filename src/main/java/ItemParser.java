import java.util.List;

public interface ItemParser {

    String FILE_PREFIX = "resources/";

    List<ItemModel> getParsedRecords(String fileName);
}
