import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Streaming-bases json parser.
 *
 * @author Sajad
 */
public class StreamingJsonParser implements ItemParser {

    @Override
    public List<ItemModel> getParsedRecords(String fileName) {
        List<ItemModel> itemModels = new ArrayList<>();
        var itemModel = new ItemModel();

        try (JsonParser jParser = new JsonFactory().createParser(new File(fileName))) {
            // loop until token equal to "}"
            while (jParser.nextToken() != JsonToken.END_OBJECT) {

                String fieldName = jParser.getCurrentName();

                if ("saleObjects".equals(fieldName)) {
                    if (jParser.nextToken() == JsonToken.START_ARRAY) {
                        // loop over each object
                        JsonToken token = jParser.getCurrentToken();
                        var goingNested = 0;
                        var current = "";
                        while (token != JsonToken.END_ARRAY) {
                            if (token == JsonToken.START_OBJECT)
                                goingNested++;

                            current = jParser.getText();

                            setCommonAttributes(current, jParser, itemModel);

                            if (token == JsonToken.END_OBJECT) {
                                goingNested--;
                                if (goingNested == 0) {
                                    itemModels.add(itemModel);
                                    itemModel = new ItemModel();
                                }
                            }

                            token = jParser.nextToken();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemModels;
    }

    private void setCommonAttributes(String value, JsonParser jParser, ItemModel itemModel) throws IOException {
        switch (value) {
            case "id":
                jParser.nextToken();
                itemModel.setId(Integer.parseInt(jParser.getText()));
                break;
            case "type":
                jParser.nextToken();
                ItemModel.ItemType type = jParser.getText().equals("APT") ? ItemModel.ItemType.APPARTEMENT : ItemModel.ItemType.valueOf(jParser.getText());
                itemModel.setType(type);
                break;
            case "sizeSqm":
                jParser.nextToken();
                itemModel.setSquareMeters(Integer.parseInt(jParser.getText()));
                break;
            case "startingPrice":
                jParser.nextToken();
                itemModel.setStartingPrice(jParser.getText());
                itemModel.setPricePerSquareMeter(Helper.getPricePerSquareMeter(itemModel.getStartingPrice(), itemModel.getSquareMeters()));
                break;
            case "city":
                jParser.nextToken();
                itemModel.setCity(jParser.getText());
                break;
            case "street":
                jParser.nextToken();
                itemModel.setStreet(jParser.getText());
                break;
            case "floor":
                jParser.nextToken();
                itemModel.setFloor(Integer.valueOf(jParser.getText()));
                break;
        }
    }
}
