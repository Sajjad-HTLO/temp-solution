
import java.util.Comparator;

public class ItemComparator implements Comparator<ItemModel> {

    private final SaleObjectConsumer.PriorityOrderAttribute orderAttribute;

    public ItemComparator(SaleObjectConsumer.PriorityOrderAttribute orderAttribute) {
        this.orderAttribute = orderAttribute;
    }

    @Override
    public int compare(ItemModel o1, ItemModel o2) {
        return switch (orderAttribute) {
            case SquareMeters -> o1.getSquareMeters() - o2.getSquareMeters();
            case City -> o1.getCity().compareTo(o2.getCity());
            case PricePerSquareMeter -> o1.getPricePerSquareMeter() - o2.getPricePerSquareMeter();
        };
    }
}
