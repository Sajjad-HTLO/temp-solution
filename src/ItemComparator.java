import java.util.Comparator;

public class ItemComparator implements Comparator<ItemModel> {

    private final SaleObjectConsumer.PriorityOrderAttribute orderAttribute;

    public ItemComparator(SaleObjectConsumer.PriorityOrderAttribute orderAttribute) {
        this.orderAttribute = orderAttribute;
    }

    @Override
    public int compare(ItemModel o1, ItemModel o2) {
        switch (orderAttribute) {
            case SquareMeters:
                return o1.getSquareMeters() - o2.getSquareMeters();
            case City:
                return o1.getCity().compareTo(o2.getCity());
            case PricePerSquareMeter:
                return (int) (o1.getPricePerSquareMeter() - o2.getPricePerSquareMeter());
        }

        throw new SaleObjectConsumer.TechnicalException();
    }
}
