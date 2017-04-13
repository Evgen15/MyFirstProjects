package logic;

/**
 * Класс-сущность "продавец"
 */
public class Seller {
    private int id;
    private String name;
    private String address;

    public Seller() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
