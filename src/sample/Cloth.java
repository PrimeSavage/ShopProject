package sample;

public class Cloth {
    private int serial;
    private int price;
    private String color;
    private String type;

    public Cloth(int serial, String color, String type, int price) {
        this.serial = serial;
        this.price = price;
        this.color = color;
        this.type = type;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

