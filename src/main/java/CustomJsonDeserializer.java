import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomJsonDeserializer extends StdDeserializer {

    public CustomJsonDeserializer() {
        this(null);
    }

    public CustomJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<ItemModel> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        ArrayNode arrayNode = (ArrayNode) node.get("saleObjects");

        for (JsonNode jsonNode : arrayNode) {
            ItemModel.ItemType type = Helper.resolveType(jsonNode.get("type").textValue());

            int sqmSize = jsonNode.get("sizeSqm").asInt();
            String startingPrice = jsonNode.get("startingPrice").asText();

            int pricePerSquare = Helper.getPricePerSquareMeter(startingPrice, sqmSize);

            JsonNode addressNode = jsonNode.get("postalAddress");

            String city = addressNode.get("city").asText();
            String street = addressNode.get("street").asText();
            Integer floor = null;

            if (addressNode.get("floor") != null)
                floor = addressNode.get("floor").asInt();

            ItemModel itemModel = new ItemModel(type, pricePerSquare, city, street, sqmSize, floor);
            result.add(itemModel);
        }

        return result;
    }
}
