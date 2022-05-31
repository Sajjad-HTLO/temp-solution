/**
 * Data class to hold a record
 */
public class ItemModel {

    public ItemModel() {
    }

    public ItemModel(int pricePerSquareMeter, String city, String street, int squareMeters, Integer floor) {
        this.pricePerSquareMeter = pricePerSquareMeter;
        this.city = city;
        this.street = street;
        this.squareMeters = squareMeters;
        this.floor = floor;
    }

    private int pricePerSquareMeter;
    private String city;
    private String street;
    private int squareMeters;
    private Integer floor;

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

    public float getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public void setPricePerSquareMeter(int pricePerSquareMeter) {
        this.pricePerSquareMeter = pricePerSquareMeter;
    }
}
