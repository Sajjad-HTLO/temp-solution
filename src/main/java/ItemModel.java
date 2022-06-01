/**
 * Data class to hold a record
 */
public class ItemModel {

    public ItemModel() {
    }

    public ItemModel(ItemType type, int pricePerSquareMeter, String city, String street, int squareMeters, Integer floor) {
        this.type = type;
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.city = city;
        this.street = street;
        this.squareMeters = squareMeters;
        this.floor = floor;
    }

    private ItemType type;
    private int pricePerSquareMeter;
    private String city;
    private String street;
    private int squareMeters;
    private Integer floor;
    private String startingPrice;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public int getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public void setPricePerSquareMeter(int pricePerSquareMeter) {
        this.pricePerSquareMeter = pricePerSquareMeter;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    enum ItemType {
        APPARTEMENT, HOUSE
    }
}
