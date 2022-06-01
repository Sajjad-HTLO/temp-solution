import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Json parser.
 *
 * @author Sajad
 */
public class JsonItemParser implements ItemParser {

    @Override
    public List<ItemModel> getParsedRecords(String fileName) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)));

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            SimpleModule module = new SimpleModule();
            module.addDeserializer(List.class, new CustomJsonDeserializer());
            mapper.registerModule(module);

            JavaType customClassCollection = mapper.getTypeFactory().constructCollectionLikeType(List.class, ItemModel.class);
            List<ItemModel> itemModels = mapper.readValue(jsonContent, customClassCollection);

            return itemModels;

        } catch (IOException e) {
            e.printStackTrace();
            throw new SaleObjectConsumer.TechnicalException();
        }
    }
}
