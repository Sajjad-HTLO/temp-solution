import java.util.List;

public interface ItemParser {

    String FILE_PREFIX = "resources/";

    int BUCKET_SIZE = 1000000;

    List<ItemModel> getParsedRecords(String fileName);
}
